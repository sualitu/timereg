package dk.sual.timereg.pages

import dk.sual.timereg.model._
import dk.sual.timereg.pages._
import dk.sual.timereg.Helpers.Pages.baseUrl

object Tables {
  /**
    * The abstract idea of a table of something
    * 
    * @param A - the type of the content in the table
    */
  trait Table[A] extends PrettyPrint {
    /**
      * Renders a table of A's
      */
    def renderTable(content: List[A]) : String = {
      val s = new StringBuilder()
      s.append(tableStart).append(rows(content)).append(tableEnd)
      s.toString()
    }

    /**
      * How to render the header of the table
      */
    protected def tableHeader : String

    /**
      * The start of the table.
      */
    protected val tableStart : String = {
      s"""
<table id="table-style" height="400" row-style="rowStyle" class="table table-hover table-condensed">
    <thead>
    <tr>
      $tableHeader
    </tr>
    </thead>
    <tbody>
"""
    }

    /**
      * The end of the table
      */
    protected def tableEnd : String = {
"""
</tbody>
</table>
<script>
    function rowStyle(row, index) {
        var classes = ['active', 'success', 'info', 'warning', 'danger'];

        if (index % 2 === 0 && index / 2 < classes.length) {
            return {
                classes: classes[index / 2]
            };
        }
        return {};
    }
</script>
"""
    }

    /** 
      * How to render the content(rows) of the table
      */
    protected def rows(content: List[A]) : String

  }

  trait CustomerTable extends Table[Customer] {
    protected override def tableHeader : String = {
      """
        <th class="col-md-2">ID</th>
        <th class="col-md-6">
            Name
        </th>
        <th class="col-md-8">
            Description
        </th>
        <th class="col-md-2">Delete</th>
      """
    }

    protected override def rows(cs: List[Customer]) : String = {
      val s = new StringBuilder()
      var i = 1
      cs foreach { x =>
        s.append(row(x, i))
        i = i + 1
      }
      s.toString()
    }

    protected def row(customer: Customer, rowCount: Int) : String = {
      s"""
    <tr id="tr_id_$rowCount" class="tr-class-$rowCount">
        <td class="col-md-2" id="td_id_$rowCount" class="td-class-$rowCount"><a href="${baseUrl}customer/${customer.id}">${customer.id}</a></td>
        <td class="col-md-6" id="td_id_$rowCount" class="td-class-$rowCount"><a href="${baseUrl}customer/${customer.id}">${customer.name}</a></td>
        <td class="col-md-8">
            ${(customer.description) getOrElse "Nothing"}
        </td>
        <td class="col-md-2">
           <form action="${baseUrl}customer/${customer.id}/delete" method="POST"><button type="submit">X</button></form> 
        </td>
    </tr>
      """
    }
  }

  trait ProjectTable extends Table[Project] {

    protected override def tableHeader : String = {
      """
        <th class="col-md-2">ID</th>
        <th class="col-md-6">Customer</th>
        <th class="col-md-6">
            Name
        </th>
        <th class="col-md-8">
            Description
        </th>
        <th class="col-md-2">Delete</th>
      """
    }

    protected override def rows(ps : List[Project]) : String = {
      val s = new StringBuilder()
      var i = 1
      ps foreach { x =>
        s.append(row(x, i))
        i = i + 1
      }
      s.toString()
    }

    protected def row(project: Project, rowCount: Int) : String = {
      s"""
    <tr id="tr_id_$rowCount" class="tr-class-$rowCount">
        <td class="col-md-2" id="td_id_$rowCount" class="td-class-$rowCount"><a href="${baseUrl}project/${project.id}">${project.id}</a></td>
        <td class="col-md-6">
           ${showOptionCust(project.customer)}
        </td>
        <td class="col-md-6" id="td_id_$rowCount" class="td-class-$rowCount"><a href="${baseUrl}project/${project.id}">${project.name}</a></td>
        <td class="col-md-8">
            ${(project.description).getOrElse("Nothing")}
        </td>
        <td class="col-md-2">
           <form action="${baseUrl}project/${project.id}/delete" method="POST"><button type="submit">X</button></form> 
        </td>

    </tr>
"""
    }
  }

  trait RateTable extends Table[Rate] {

    protected override def tableHeader : String = {
      """
        <th class="col-md-2">ID</th>
        <th class="col-md-6">Name</th>
        <th class="col-md-6">
            Rate
        </th>

        <th class="col-md-2">Delete</th>
      """
    }

    protected override def rows(ps : List[Rate]) : String = {
      val s = new StringBuilder()
      var i = 1
      ps foreach { x =>
        s.append(row(x, i))
        i = i + 1
      }
      s.toString()
    }

    protected def row(rate: Rate, rowCount: Int) : String = {
      s"""
    <tr id="tr_id_$rowCount" class="tr-class-$rowCount">
        <td class="col-md-2" id="td_id_$rowCount" class="td-class-$rowCount"><a href="${baseUrl}project/${rate.id}">${rate.id}</a></td>
        <td class="col-md-6">
           ${rate.name}
        </td>
        <td class="col-md-6">
           ${rate.rate}
        </td>
        <td class="col-md-2">
           <form action="${baseUrl}rate/${rate.id}/delete" method="POST"><button type="submit">X</button></form> 
        </td>

    </tr>
"""
    }
  }

  trait RegistrationTable extends Table[Registration] {

    protected override def tableHeader : String = {
      """
        <th class="col-md-6">Project</th>
        <th class="col-md-6">
            Count
        </th>
        <th class="col-md-6">
            Rate
        </th>
        <th class="col-md-6">
            Total
        </th>
        <th class="col-md-2">Delete</th>
      """
    }

    protected override def rows(ps : List[Registration]) : String = {
      val s = new StringBuilder()
      var i = 1
      ps foreach { x =>
        s.append(row(x, i))
        i = i + 1
      }
      s.toString()
    }

    protected def row(registration: Registration, rowCount: Int) : String = {
      s"""
    <tr id="tr_id_$rowCount" class="tr-class-$rowCount">
        <td class="col-md-6">
           ${showOptionProj(registration.project)}
        </td>
        <td class="col-md-6">
           ${registration.count}
        </td>
        <td class="col-md-6">
           ${showOptionRate(registration.rate)}
        </td>
        <td class="col-md-6">
           ${registration.amount getOrElse "Not available"}
        </td>
        <td class="col-md-2">
           <form action="${baseUrl}registration/${registration.id}/delete" method="POST"><button type="submit">X</button></form> 
        </td>

    </tr>
"""
    }
  }

  /**
    * A table of registrations used for making invoices.
    */
  trait InvoiceTable extends Table[Registration] {

    protected override def tableHeader : String = {
      """
        <th class="col-md-6">
            Count
        </th>
        <th class="col-md-6">
            Rate
        </th>
        <th class="col-md-6">
            Total
        </th>
      """
    }

    protected override def rows(ps : List[Registration]) : String = {
      val s = new StringBuilder()
      var i = 1
      var total = 0
      ps foreach { x =>
        s.append(row(x, i))
        total = total + { x.amount getOrElse 0 }
        i = i + 1
      }
      s.append(invoiceEnd(total, i))
      s.toString()
    }

    protected def row(registration: Registration, rowCount: Int) : String = {
      s"""
    <tr id="tr_id_$rowCount" class="tr-class-$rowCount">
        <td class="col-md-6">
           ${registration.count}
        </td>
        <td class="col-md-6">
           ${showOptionRate(registration.rate)}
        </td>
        <td class="col-md-6">
           ${registration.amount getOrElse "0"}
        </td>
    </tr>
"""
    }

    protected def invoiceEnd(total: Int, rowCount: Int) : String = {
      val s = new StringBuilder()
      val totalLine = 
      s"""
    <tr id="tr_id_$rowCount" class="tr-class-$rowCount">
        <td class="col-md-6">
          Final
        </td>
        <td class="col-md-6">

        </td>
        <td class="col-md-6">
           ${total}
        </td>
    </tr>
"""
      s.append(totalLine)
      s.append(super.tableEnd)
      s.toString()
    }
    protected override def tableEnd : String = ""
  }
}
