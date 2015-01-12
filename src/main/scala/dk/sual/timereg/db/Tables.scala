package dk.sual.timereg.db

/*
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = scala.slick.driver.H2Driver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: scala.slick.driver.JdbcProfile
  import profile.simple._
  import scala.slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import scala.slick.jdbc.{GetResult => GR}
  
  /** DDL for all tables. Call .create to execute. */
  lazy val ddl = Customer.ddl ++ Project.ddl ++ Rate.ddl ++ Registration.ddl ++ User.ddl
  
  /** Entity class storing rows of table Customer
   *  @param owner Database column OWNER DBType(INTEGER)
   *  @param name Database column NAME DBType(VARCHAR), Length(255,true)
   *  @param description Database column DESCRIPTION DBType(CLOB)
   *  @param rid Database column RID DBType(INTEGER), AutoInc, PrimaryKey */
  case class CustomerRow(owner: Int, name: Option[String], description: Option[java.sql.Clob], rid: Int)
  /** GetResult implicit for fetching CustomerRow objects using plain SQL queries */
  implicit def GetResultCustomerRow(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[Option[java.sql.Clob]]): GR[CustomerRow] = GR{
    prs => import prs._
    CustomerRow.tupled((<<[Int], <<?[String], <<?[java.sql.Clob], <<[Int]))
  }
  /** Table description of table CUSTOMER. Objects of this class serve as prototypes for rows in queries. */
  class Customer(_tableTag: Tag) extends Table[CustomerRow](_tableTag, "CUSTOMER") {
    def * = (owner, name, description, rid) <> (CustomerRow.tupled, CustomerRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (owner.?, name, description, rid.?).shaped.<>({r=>import r._; _1.map(_=> CustomerRow.tupled((_1.get, _2, _3, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column OWNER DBType(INTEGER) */
    val owner: Column[Int] = column[Int]("OWNER")
    /** Database column NAME DBType(VARCHAR), Length(255,true) */
    val name: Column[Option[String]] = column[Option[String]]("NAME", O.Length(255,varying=true))
    /** Database column DESCRIPTION DBType(CLOB) */
    val description: Column[Option[java.sql.Clob]] = column[Option[java.sql.Clob]]("DESCRIPTION")
    /** Database column RID DBType(INTEGER), AutoInc, PrimaryKey */
    val rid: Column[Int] = column[Int]("RID", O.AutoInc, O.PrimaryKey)
    
    /** Foreign key referencing User (database name CONSTRAINT_5) */
    lazy val userFk = foreignKey("CONSTRAINT_5", owner, User)(r => r.rid, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table Customer */
  lazy val Customer = new TableQuery(tag => new Customer(tag))
  
  /** Entity class storing rows of table Project
   *  @param customer Database column CUSTOMER DBType(INTEGER)
   *  @param user Database column USER DBType(INTEGER)
   *  @param name Database column NAME DBType(VARCHAR), Length(255,true)
   *  @param description Database column DESCRIPTION DBType(CLOB)
   *  @param rid Database column RID DBType(INTEGER), AutoInc, PrimaryKey */
  case class ProjectRow(customer: Int, user: Int, name: Option[String], description: Option[java.sql.Clob], rid: Int)
  /** GetResult implicit for fetching ProjectRow objects using plain SQL queries */
  implicit def GetResultProjectRow(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[Option[java.sql.Clob]]): GR[ProjectRow] = GR{
    prs => import prs._
    ProjectRow.tupled((<<[Int], <<[Int], <<?[String], <<?[java.sql.Clob], <<[Int]))
  }
  /** Table description of table PROJECT. Objects of this class serve as prototypes for rows in queries. */
  class Project(_tableTag: Tag) extends Table[ProjectRow](_tableTag, "PROJECT") {
    def * = (customer, user, name, description, rid) <> (ProjectRow.tupled, ProjectRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (customer.?, user.?, name, description, rid.?).shaped.<>({r=>import r._; _1.map(_=> ProjectRow.tupled((_1.get, _2.get, _3, _4, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column CUSTOMER DBType(INTEGER) */
    val customer: Column[Int] = column[Int]("CUSTOMER")
    /** Database column USER DBType(INTEGER) */
    val user: Column[Int] = column[Int]("USER")
    /** Database column NAME DBType(VARCHAR), Length(255,true) */
    val name: Column[Option[String]] = column[Option[String]]("NAME", O.Length(255,varying=true))
    /** Database column DESCRIPTION DBType(CLOB) */
    val description: Column[Option[java.sql.Clob]] = column[Option[java.sql.Clob]]("DESCRIPTION")
    /** Database column RID DBType(INTEGER), AutoInc, PrimaryKey */
    val rid: Column[Int] = column[Int]("RID", O.AutoInc, O.PrimaryKey)
    
    /** Foreign key referencing Customer (database name CONSTRAINT_18) */
    lazy val customerFk = foreignKey("CONSTRAINT_18", customer, Customer)(r => r.rid, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing User (database name CONSTRAINT_1) */
    lazy val userFk = foreignKey("CONSTRAINT_1", user, User)(r => r.rid, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table Project */
  lazy val Project = new TableQuery(tag => new Project(tag))
  
  /** Entity class storing rows of table Rate
   *  @param rate Database column RATE DBType(INTEGER)
   *  @param description Database column DESCRIPTION DBType(CLOB)
   *  @param rid Database column RID DBType(INTEGER), AutoInc, PrimaryKey */
  case class RateRow(rate: Option[Int], description: Option[java.sql.Clob], rid: Int)
  /** GetResult implicit for fetching RateRow objects using plain SQL queries */
  implicit def GetResultRateRow(implicit e0: GR[Option[Int]], e1: GR[Option[java.sql.Clob]], e2: GR[Int]): GR[RateRow] = GR{
    prs => import prs._
    RateRow.tupled((<<?[Int], <<?[java.sql.Clob], <<[Int]))
  }
  /** Table description of table RATE. Objects of this class serve as prototypes for rows in queries. */
  class Rate(_tableTag: Tag) extends Table[RateRow](_tableTag, "RATE") {
    def * = (rate, description, rid) <> (RateRow.tupled, RateRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (rate, description, rid.?).shaped.<>({r=>import r._; _3.map(_=> RateRow.tupled((_1, _2, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column RATE DBType(INTEGER) */
    val rate: Column[Option[Int]] = column[Option[Int]]("RATE")
    /** Database column DESCRIPTION DBType(CLOB) */
    val description: Column[Option[java.sql.Clob]] = column[Option[java.sql.Clob]]("DESCRIPTION")
    /** Database column RID DBType(INTEGER), AutoInc, PrimaryKey */
    val rid: Column[Int] = column[Int]("RID", O.AutoInc, O.PrimaryKey)
  }
  /** Collection-like TableQuery object for table Rate */
  lazy val Rate = new TableQuery(tag => new Rate(tag))
  
  /** Entity class storing rows of table Registration
   *  @param duration Database column DURATION DBType(INTEGER)
   *  @param project Database column PROJECT DBType(INTEGER)
   *  @param rate Database column RATE DBType(INTEGER)
   *  @param rid Database column RID DBType(INTEGER), AutoInc, PrimaryKey */
  case class RegistrationRow(duration: Option[Int], project: Int, rate: Int, rid: Int)
  /** GetResult implicit for fetching RegistrationRow objects using plain SQL queries */
  implicit def GetResultRegistrationRow(implicit e0: GR[Option[Int]], e1: GR[Int]): GR[RegistrationRow] = GR{
    prs => import prs._
    RegistrationRow.tupled((<<?[Int], <<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table REGISTRATION. Objects of this class serve as prototypes for rows in queries. */
  class Registration(_tableTag: Tag) extends Table[RegistrationRow](_tableTag, "REGISTRATION") {
    def * = (duration, project, rate, rid) <> (RegistrationRow.tupled, RegistrationRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (duration, project.?, rate.?, rid.?).shaped.<>({r=>import r._; _2.map(_=> RegistrationRow.tupled((_1, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column DURATION DBType(INTEGER) */
    val duration: Column[Option[Int]] = column[Option[Int]]("DURATION")
    /** Database column PROJECT DBType(INTEGER) */
    val project: Column[Int] = column[Int]("PROJECT")
    /** Database column RATE DBType(INTEGER) */
    val rate: Column[Int] = column[Int]("RATE")
    /** Database column RID DBType(INTEGER), AutoInc, PrimaryKey */
    val rid: Column[Int] = column[Int]("RID", O.AutoInc, O.PrimaryKey)
    
    /** Foreign key referencing Project (database name CONSTRAINT_39) */
    lazy val projectFk = foreignKey("CONSTRAINT_39", project, Project)(r => r.rid, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing Rate (database name CONSTRAINT_3) */
    lazy val rateFk = foreignKey("CONSTRAINT_3", rate, Rate)(r => r.rid, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table Registration */
  lazy val Registration = new TableQuery(tag => new Registration(tag))
  
  /** Entity class storing rows of table User
   *  @param name Database column NAME DBType(VARCHAR), Length(255,true)
   *  @param password Database column PASSWORD DBType(VARCHAR), Length(255,true)
   *  @param rid Database column RID DBType(INTEGER), AutoInc, PrimaryKey */
  case class UserRow(name: Option[String], password: Option[String], rid: Int)
  /** GetResult implicit for fetching UserRow objects using plain SQL queries */
  implicit def GetResultUserRow(implicit e0: GR[Option[String]], e1: GR[Int]): GR[UserRow] = GR{
    prs => import prs._
    UserRow.tupled((<<?[String], <<?[String], <<[Int]))
  }
  /** Table description of table USER. Objects of this class serve as prototypes for rows in queries. */
  class User(_tableTag: Tag) extends Table[UserRow](_tableTag, "USER") {
    def * = (name, password, rid) <> (UserRow.tupled, UserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (name, password, rid.?).shaped.<>({r=>import r._; _3.map(_=> UserRow.tupled((_1, _2, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column NAME DBType(VARCHAR), Length(255,true) */
    val name: Column[Option[String]] = column[Option[String]]("NAME", O.Length(255,varying=true))
    /** Database column PASSWORD DBType(VARCHAR), Length(255,true) */
    val password: Column[Option[String]] = column[Option[String]]("PASSWORD", O.Length(255,varying=true))
    /** Database column RID DBType(INTEGER), AutoInc, PrimaryKey */
    val rid: Column[Int] = column[Int]("RID", O.AutoInc, O.PrimaryKey)
  }
  /** Collection-like TableQuery object for table User */
  lazy val User = new TableQuery(tag => new User(tag))
}*/
