# vaadin-flow-bugs
Demonstration for [Vaadin Flow Bug #14239](https://github.com/vaadin/flow/issues/14239)

How to run this:

1. Run `mvn package jetty:run`
1. Point browser at `http://localhost:8090/demo/demo/` (note trailing slash)

You will see that the Javascript highlighting add-on is not working.

In Firefox the inspector console will show the underlying errors:

```
Loading failed for the <script> with source “http://localhost:8090/demo/demo/ace-builds/src-min-noconflict/theme-chrome.js”.
Loading failed for the <script> with source “http://localhost:8090/demo/demo/ace-builds/src-min-noconflict/mode-javascript.js”.
```

