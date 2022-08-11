# vaadin-flow-bugs
Demonstration for [Vaadin Flow Bug #14239](https://github.com/vaadin/flow/issues/14239)

How to run this:

1. Run `mvn package jetty:run`
1. Point browser at `http://localhost:8090/demo/demo/` (note trailing slash)

To apply the partial workaround:

1. Edit `src/main/webapp/WEB-INF/web.xml` and uncomment the stuff under "(PARTIAL) WORKAROUND FOR BUG #14239"

But then you'll see that the add-on still doesn't work.
