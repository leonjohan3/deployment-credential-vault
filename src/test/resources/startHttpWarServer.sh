daemon() {
  $JAVA_HOME/bin/java -Xmx96M -cp target/test-classes org.dcv.HttpWarServer
}
daemon >> /dev/null 2>&1 &
disown
