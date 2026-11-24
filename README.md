# vaadin-flow-bugs
Reproducer for https://github.com/vaadin/flow/issues/27244

Reproducer for this issue: https://github.com/vaadin/flow/issues/27244

How to run this and reproduce the bug (MacOS only):

1. Verify Tomcat installed via homebrew as "tomcat"; if not edit `run.sh` and change `TOMCAT_NAME`
1. Verify you are OK with the log file `${HOME}/vbug.log` being created
1. Run the shell script `run.sh` and wait for `vbug.war` to fully deploy (takes up to 30 seconds)
1. Point your web browser at `http://localhost:8080/vbug/api/blah`
1. Notice the `500 Server Error` and corresponding exception thrown
