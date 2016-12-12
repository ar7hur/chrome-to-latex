(ns copy-as-latex.content
  (:require [clojure.string :as s]))

(def separator "%-----------------------------------------------------------------\n")

(defn sq
  "Make nodelist a sequence"
  [nodelist]
    (for [k (range (.-length nodelist))]
      (.item nodelist k)))

(def logs (atom []))

(defn warn! [node-name]
  (let [s (str "Unsupported node!" node-name)]
    (js/console.log s)
    (swap! logs conj s)))

(defn transform-node [node f]
  (case (.-nodeName node)
    ("P" "DIV")   (str "\n" (f) "\n")
    ("SPAN")      (str " " (f) " ")
    ("A")         (f)
    ("I", "EM", "B", "U")   (str " \\emph{" (f) "} ")
    ("BR")        "\n"
    
    ; else
    (do (warn! (.-nodeName node))
        (f))))

; the second group capture the last char before } to remove it
; if it's a space
(defn space-in-emph [[match g1 g2]]
  (let [l (if (= " " g2) "" g2)]
    (str "{" g1 l "}")))

(defn footnote [[match g1]]
  (if (#{"..." "…"} g1)
    " \\el "
    (str "\\footnote{" g1 "}")))

(defn final-clean [txt]
  (-> txt
      (s/replace #"[ \t\xA0]+" " ") ; non breaking space
      (s/replace #"\n +" "\n")
      (s/replace #"[\r\n]{2,}" "\n\n")
      (s/replace #"\[ " "[")
      (s/replace #"\( " "(")
      (s/replace #" \]" "]")
      (s/replace #" \)" ")")
      (s/replace #"\} ," "},")
      (s/replace #" »" "\"")
      (s/replace #"« " "\"")
      (s/replace #" *\[([^\]]+)\]" footnote) ; [] -> footnote
      (s/replace #"\" ?\\emph\{([^\}]+)\} ?\"" "\"$1\"") ; double quote
      (s/replace #"\{ *([^\}]+)([^\}])\}" space-in-emph)
      (s/replace #" ([,\.])" "$1") ; "hi , alex -> hi, alex"
      (s/replace #" - " " \u2014 ")
      (s/replace #"%" "\\%")
  ))

(defn copy-to-clipboard [txt]
  (aset js/document "oncopy" (fn [event]
                               (-> event .-clipboardData (.setData "text/plain" txt))
                               (.preventDefault event)))
  (js/document.execCommand "copy" false nil)
  (aset js/document "oncopy" nil))

(defn walk [node]
  (case (.-nodeType node)
    3 (.-textContent node)
    1 (transform-node node #(apply str (map walk (sq (.-childNodes node)))))
    " "))

(defn add-title [article title]
  (str separator "\\Chapter{" title "}{}\n\n" article))

(defn copy []
  (reset! logs [])
  (let [post (js/document.getElementById "articles_show_contenu_article")
        title (.-textContent (js/document.getElementById "articles_show_titre_article"))
        article (-> post walk final-clean (add-title title))]
    (copy-to-clipboard article)
    {:article article
     :title title
     :logs @logs}))

(defn messages []
    (.addListener js/chrome.runtime.onMessage 
      (fn [message sender reply-fn]
        (reply-fn (clj->js (copy)))
        true)))

(defn init []
  (messages))
