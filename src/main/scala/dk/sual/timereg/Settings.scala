package dk.sual.timereg

object Settings {
  object db {
    val dbServer = "localhost"
    val dbDriver = "com.mysql.jdbc.Driver"
    val db = "timereg"
    val dbUser = "root"
    val dbPassword = ""
    val dbString = s"jdbc:mysql://$dbServer/$db?user=$dbUser&password=$dbPassword"
    val dbReset = true
  }
  object pages {
    def baseUrl = "http://localhost:8080/"
  }
  object misc {
  }
}
