package dk.sual.timereg

import dk.sual.timereg.model._

object Security {
  import org.mindrot.jbcrypt.BCrypt
  import java.security.SecureRandom
  import java.math.BigInteger

  private val random = new SecureRandom()

  /**
    * Hashes a password
    * 
    * @param password - the password
    * @return The password and a salt
    */
  def hashPassword(password: String) : (String, String) = {
    val salt = new BigInteger(130, random).toString(16)

    (BCrypt.hashpw(password, BCrypt.gensalt(12, random)), salt)
  }

  /**
    * Checks if a given password matches that of a user
    * 
    * @param password - the password
    * @param user - the user
    * @return True if they match. False otherwise.
    */
  def checkPassword(password: String, user: User) : Boolean = {
    BCrypt.checkpw(password, user.password)
  }
}
