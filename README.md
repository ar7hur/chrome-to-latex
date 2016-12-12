## Disclaimer

This is by no mean a feature-complete HTML to LaTeX converter. It just covers what I needed for my project, mostly simple text styling. It should be easy to extend for your own needs though.

## How to use

  1. Build the extension with `lein chromebuild once`
	```bash
	lein chromebuild once
	Picked up JAVA_TOOL_OPTIONS: -Dfile.encoding=UTF8
	Compiling ClojureScript...
	Picked up JAVA_TOOL_OPTIONS: -Dfile.encoding=UTF8
	Compiling "target/unpacked/copy_as_latex.js" from ["src"]...
	Successfully compiled "target/unpacked/copy_as_latex.js" in 3.599 seconds.
	Built extension to target/unpacked
	```

  2. Load in Chrome
  `Windows` -> `Extensions` -> `Load unpacked extension...` -> Select the `target/unpacked` directory from the project

  3. Turn HTML into LaTeX
    - Visit the web page
    - Click on the extension icon ![icon](resources/images/icon48.png)
    - The extension popup will display the LaTeX code (and potential warnings) and copy it to the clipboard

    **(The ID of the DOM node whose content is turned into LaTeX is hard-coded in `src/content.cljs` so it won't work out of the box on any website!)**
