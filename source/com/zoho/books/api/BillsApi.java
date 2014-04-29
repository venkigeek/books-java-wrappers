/* $Id$ */

package com.zoho.books.api;

import com.zoho.books.util.ZohoHTTPClient;
import com.zoho.books.util.BooksUtil;

import com.zoho.books.parser.BillParser;

import com.zoho.books.model.Bill;
import com.zoho.books.model.LineItem;
import com.zoho.books.model.Address;
import com.zoho.books.model.Payment;
import com.zoho.books.model.Comment;
import com.zoho.books.model.BillList;
import com.zoho.books.model.CommentList;
import com.zoho.books.model.PaymentList;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.util.HashMap;

import org.json.JSONObject;
import org.json.JSONArray;

/**

* BillsApi is used to create a Bill for the vendor.

* It is used to get:<br><br>

	The list of all the bills.<br>
	
	The details of a bill.<br>
	
	The list of bill payments.<br>
	
	The bill attchment.<br>
	
	The list of comments for the bill.<br><br>
	
* It is used to:<br><br>
	
	Apply credits to a bill.<br>
	
	Add an attachment to a bill.<br>
	
	Add a comment to a bill.<br><br>
	
* It is used to update:<br><br>

	The details of a bill.<br>
	
	The billing address of a bill.<br><br>
	
* It is used to change the status:<br><br>

	Mark a bill status as void.<br>
	
	Mark a void bill as open.<br><br>
	
* It is used to delete:<br><br>

	The particular bill.<br>
	
	The payment made to a bill.<br>
	
	The file attached to a bill.<br>
	
	The bill comment.<br>

*/

public class BillsApi
{

	private static String url = BooksUtil.baseURL+"/bills"; //No I18N

	private String authToken;
	private String organizationId;
	
	/**
	
	* Construct a new BillsApi using user's authtoken and organizationid.
	
	* @param authToken user's authToken. 
	
	* @param organizationId user's organization id.
	
	*/	

	public BillsApi(String authToken, String organizationId)
	{
		this.authToken = authToken;
		this.organizationId = organizationId;
	}
	
	
	//private ZohoHTTPClient ZohoHTTPClient = new ZohoHTTPClient();
	
	private BillParser billParser = new BillParser();
	
	
	
	/**
	
	* Create a bill received from your vendor.
	
	* Pass the vendorId, billNumber, and accountId to create a new bill for the vendor.
	
	* It returns the Bill object.
	
	
	* @param vendorId ID of the vendor for whom the bill has to be created.
	
	* @param billNumber The bill number.
	
	* @param accountId ID of the account associated with the line item.
	
	* @return Returns the Bill object.
	
	*/
	
	public Bill create(String vendorId, String billNumber, String accountId)throws Exception
	{
		
		HashMap requestBody = new HashMap();
		
		requestBody.put("authtoken", authToken);
		requestBody.put("organization_id", organizationId);
		
		
		Bill billObj = new Bill();
		
		billObj.setVendorId(vendorId);
		billObj.setBillNumber(billNumber);
		
		List<LineItem> lineItems = new ArrayList<LineItem>();
		
		LineItem lineItem = new LineItem();
		
		lineItem.setAccountId(accountId);
		
		lineItems.add(0, lineItem);
		
		billObj.setLineItems(lineItems);

		
		requestBody.put("JSONString", billObj.toJSON().toString());
		
		String response = ZohoHTTPClient.post(url, requestBody);
		
		Bill bill = billParser.getBill(response);
		
		return bill;
	}
	
	/**
	
	* Create a bill received from your vendor.
	
	* Pass the Bill object to create a new bill for the vendor.
	
	* The Bill object which contains vendorId, billNumber, and accountId are the mandatory parameters.
	
	* It returns the Bill object.
	
	
	* @param bill Bill object.
	
	* @return Returns the Bill object.
	
	*/
	
	public Bill create(Bill bill)throws Exception
	{
		
		HashMap requestBody = new HashMap();
		
		requestBody.put("authtoken", authToken);
		requestBody.put("organization_id", organizationId);
		
		requestBody.put("JSONString", bill.toJSON().toString());
		
		String response = ZohoHTTPClient.post(url, requestBody);
		
		return billParser.getBill(response);
		
	}
	
	/**
	
	* Create a bill received from your vendor.
	
	* Pass the Bill object and File object to create a new bill with the attachment for the vendor.
	
	* The Bill object which contains vendorId, billNumber, and accountId are the mandatory parameters.
	
	* It returns the Bill object.
	
	
	* @param bill Bill object.
	
	* @param file File to attach.
	
	* @return Returns the Bill object.
	
	*/
	
	public Bill create(Bill bill, File file)throws Exception
	{
		
		HashMap queryMap = new HashMap();
		
		queryMap.put("authtoken", authToken);
		queryMap.put("organization_id", organizationId);
		
		HashMap requestBody = new HashMap();
		
		requestBody.put("JSONString", bill.toJSON().toString());
		
		HashMap fileBody = new HashMap();
		fileBody.put("attachment", file);
		
		String response = ZohoHTTPClient.post(url, queryMap, requestBody, fileBody);
		
		return billParser.getBill(response);
		
	}
	
	/**
	
	* Get the details of a bill.
	
	* Pass the billId to get the details of a bill.
	
	* It returns the Bill object.
	
	
	* @param billId ID of the bill.
	
	* @return Returns the Bill object.
	
	*/
	
	public Bill get(String billId)throws Exception
	{
		
		String urlString = url+"/"+billId; 
		
		HashMap queryMap = new HashMap();
		
		queryMap.put("authtoken", authToken);
		queryMap.put("organization_id", organizationId);
		
		String response = ZohoHTTPClient.get(urlString, queryMap);
		
		Bill bill = billParser.getBill(response);
		
		return bill;
	}
	
	/**
	
	* Update a bill, and delete a line item by removing it from the line_items list.
	
	* Pass the Bill object to update the details of a bill.
	
	* The Bill object which contains billId is the mandatory parameter for which bill details has to be updated.
	
	* It returns the Bill object.
	
	
	* @param bill Bill object.
	
	* @return Returns the Bill object.
	
	*/
	
	public Bill update(Bill bill)throws Exception
	{
		
		String urlString = url+"/"+bill.getBillId(); 
		
		HashMap requestBody = new HashMap();
		
		requestBody.put("authtoken", authToken);
		requestBody.put("organization_id", organizationId);
		
		requestBody.put("JSONString", bill.toJSON().toString());
		
		String response = ZohoHTTPClient.put(urlString, requestBody);
		
		return billParser.getBill(response);
	}
	
	/**
	
	* Update a bill, and delete a line item by removing it from the line_items list.
	
	* Pass the Bill object and File object to update the details and attachement of a bill.
	
	* The Bill object which contains billId is the mandatory parameter for which bill details has to be updated.
	
	* It returns the Bill object.
	
	
	* @param bill Bill object.
	
	* @param file File is an attachment(It can either be a replacement or new attachment).
	
	* @return Returns the Bill object.
	
	*/
	
	public Bill update(Bill bill, File file)throws Exception
	{
		
		String urlString = url+"/"+bill.getBillId(); 
		
		HashMap queryMap = new HashMap();
		
		queryMap.put("authtoken", authToken);
		queryMap.put("organization_id", organizationId);
		
		HashMap requestBody = new HashMap();
		
		requestBody.put("JSONString", bill.toJSON().toString());
		
		HashMap fileBody = new HashMap();
		
		fileBody.put("attachment", file);
		
		String response = ZohoHTTPClient.put(urlString, queryMap, requestBody, fileBody);
		
		return billParser.getBill(response);
	}
	
	/**
	
	* Delete an existing bill. (Bills which have payments applied cannot be deleted.)
	
	* Pass the billId to delete the bill.
	
	* If the bill has been deleted it returns the success message.
	
	* The success message is "The bill has been deleted."
	
	
	* @param billId ID of the bill.
	
	* @return Returns a String.
	
	*/
	
	public String delete(String billId)throws Exception
	{
		
		String urlString = url+"/"+billId; 
		
		HashMap queryMap = new HashMap();
		
		queryMap.put("authtoken", authToken);
		queryMap.put("organization_id", organizationId);
		
		String response = ZohoHTTPClient.delete(urlString, queryMap);
		
		String success = billParser.getMessage(response);
		
		return success;
	}
	
	/**
	
	* List all bills with pagination.
	
	* Pass the filters to get all the bills based on the filters.
	
	* It returns the BillList object.<br>
	
	* The queryMap contains the possible keys and values as mentioned below:<br><br>
	
		<table border = "1">
			
			<tbody>
			
				<tr><td>bill_number</td><td>Search bills by bill number.<br>
Variants: <i>bill_number_startswith</i> and <i>bill_number_contains</i></td></tr>
		
				<tr><td>reference_number</td><td>Search bills by reference number.<br>
Variants: <i>reference_number_startswith</i> and <i>reference_number_contains</i></td></tr>
		
				<tr><td>date</td><td>Search bills by bill date.<br>
Variants: <i>date_start, date_end, date_before</i> and <i>date.after</i></td></tr>
		
				<tr><td>description</td><td>Search bills by description.<br>
Variants: <i>description_startswith</i> and <i>description_contains</i></td></tr>
		
				<tr><td>vendor_name</td><td>Search bills by vendor name.<br>
Variants: <i>vendor_name_startswith</i> and <i>vendor_name_contains</i></td></tr>
		
				<tr><td>total</td><td>Search bills by bill total.<br>
Variants: <i>total_less_than, total_less_equals, total_greater_than</i> and <i>total_greater_equals</i></td></tr>
		
				<tr><td>vendor_id</td><td>Search bills by vendor id.</td></tr>
		
				<tr><td>item_id</td><td>Search bills by bill item id.</td></tr>
		
				<tr><td>search_text</td><td>Search bills by bill number or reference number or vendor name.</td></tr>
	
				<tr><td>status</td><td>Search bills by bill status.<br>
Allowed Values: <i>paid, open, overdue, void</i> and <i>partially_paid</i></td></tr>
		
				<tr><td>filter_by</td><td>Filter bills by any status.<br>
Allowed Values: <i>Status.All, Status.PartiallyPaid, Status.Paid, Status.Overdue, Status.Void</i> and <i>Status.Open</i></td></tr>
		
				<tr><td>sort_column</td><td>Sort bills.<br>
Allowed Values: <i>vendor_name, bill_number, date, due_date, total, balance</i> and <i>created_time</i></td></tr>
			
			</tbody>
			
		</table>
	
	* @param queryMap It contains the query string parameters in the form of key-value pair.
	
	* @return Returns the BillList object.
	
	*/
	
	public BillList getBills(HashMap queryMap)throws Exception
	{
		
		if(queryMap == null)
		{
			queryMap = new HashMap();
		}
		
		queryMap.put("authtoken", authToken);
		queryMap.put("organization_id", organizationId);
		
		String response = ZohoHTTPClient.get(url, queryMap);
		
		BillList billList = billParser.getBills(response);
		
		return billList;
	}
	
	/**
	
	* Mark a bill status as void.
	
	* Pass the billId to change the status of a bill to 'void'.
	
	* If the status of the bill has been changed it returns the success message.
	
	* The success message is "The bill has been marked as void."
	
	
	* @param billId ID of the bill.
	
	* @return Returns a String.
	
	*/
	
	public String voidABill(String billId)throws Exception
	{
		
		String urlString = url+"/"+billId+"/status/void";  //No I18N
		
		HashMap requestBody = new HashMap();
		
		requestBody.put("authtoken", authToken);
		requestBody.put("organization_id", organizationId);
		
		String response = ZohoHTTPClient.post(urlString, requestBody);
		
		String success = billParser.getMessage(response);
		
		return success;
	}
	
	/**
	
	* Mark a void bill as open.
	
	* Pass the billId to change the status of a bill to 'open'.
	
	* If the status of the bill has been changed it returns the success message.
	
	* The success message is "The status of the bill has been changed from void to open."
	
	
	* @param billId ID of the bill.
	
	* @return Returns a String.
	
	*/
	
	public String markABillAsOpen(String billId)throws Exception
	{
		
		String urlString = url+"/"+billId+"/status/open";  //No I18N
		
		HashMap requestBody = new HashMap();
		
		requestBody.put("authtoken", authToken);
		requestBody.put("organization_id", organizationId);
		
		String response = ZohoHTTPClient.post(urlString, requestBody);
		
		String success = billParser.getMessage(response);
		
		return success;
	}
	
	/**
	
	* Update the billing address for this bill.
	
	* Pass the billId and Address object to update the billing address of a bill.
	
	* If the billing address has been updated it returns the success message.
	
	* The success message is "Billing address updated."
	
	
	* @param billId ID of the bill.
	
	* @param billingAddress Address object.
	
	* @return Returns a String.
	
	*/
	
	public String updateBillingAddress(String billId, Address billingAddress)throws Exception
	{
		
		String urlString = url+"/"+billId+"/address/billing";  //No I18N
		
		HashMap requestBody = new HashMap();
		
		requestBody.put("authtoken", authToken);
		requestBody.put("organization_id", organizationId);
		
		requestBody.put("JSONString", billingAddress.toJSON().put("is_update_customer", billingAddress.isUpdateCustomer()).toString());
	
		String response = ZohoHTTPClient.put(urlString, requestBody);
		
		String success = billParser.getMessage(response);
		
		return success;
	}
	
	
//==========================================================================================================================================



	/**
	
	* Get the list of payments made for a bill.
	
	* Pass the billId to get all the payments for the bill.
	
	* It returns the PaymentList object.
	 
	
	* @param billId ID of the bill.
	
	* @return Returns list of Payment object.
	
	*/
	
	public PaymentList getPayments(String billId)throws Exception
	{	
		
		String urlString = url+"/"+billId+"/payments";  //No I18N
		
		HashMap queryMap = new HashMap();
		
		queryMap.put("authtoken", authToken);
		queryMap.put("organization_id", organizationId);
		
		String response = ZohoHTTPClient.get(urlString, queryMap);
		
		PaymentList payments = billParser.getPayments(response);
		
		return payments;
	}
	
	/**
	
	* Apply vendor credits from excess vendor payments to a bill. (Multiple credits can be applied at once.)
	
	* Pass the billId and list of Payment object to apply the credits for the bill.
	
	* The Payment object which contains amountApplied is the mandatory parameter.
	
	* If the credits has been applied to the bill it returns the success message.
	
	* The success message is "Credits have been applied to the bill(s)."
	
	
	* @param billId ID of the bill.
	
	* @param payment List of Payment object.
	
	* @return Returns a String.
	
	*/
	
	public String applyCredits(String billId, List<Payment> payment)throws Exception
	{
		
		String urlString = url+"/"+billId+"/credits";  //No I18N
		
		HashMap requestBody = new HashMap();
		
		requestBody.put("authtoken", authToken);
		requestBody.put("organization_id", organizationId);
		
		JSONObject jsonObject = new JSONObject();
		
		JSONArray billPayments = new JSONArray();
		
		for(int i = 0; i < payment.size(); i++)
		{
			JSONObject billPayment = new JSONObject();
			
			billPayment.put("payment_id", payment.get(i).getPaymentId());
			billPayment.put("amount_applied", payment.get(i).getAmountApplied());
			
			billPayments.put(billPayment);
		}
		jsonObject.put("bill_payments", billPayments);
		
		requestBody.put("JSONString", jsonObject.toString());
		
		String response = ZohoHTTPClient.post(urlString, requestBody);
		
		String success = billParser.getMessage(response);
		
		return success;
	}
	
	/**
	
	* Delete a payment made to a bill.
	
	* Pass the billId and billPaymentId to delete the bill payment.
	
	* If the bill payment has been deleted it returns the success message.
	
	* The success message is "The payment has been deleted."
	
	
	* @param billId ID of the bill.
	
	* @param billPaymentId ID of the bill payment.
	
	* @return Returns a String.
	
	*/
	
	public String deletePayment(String billId, String billPaymentId)throws Exception
	{
		
		String urlString = url+"/"+billId+"/payments/"+billPaymentId;  //No I18N
		
		HashMap queryMap = new HashMap();
		
		queryMap.put("authtoken", authToken);
		queryMap.put("organization_id", organizationId);
		
		String response = ZohoHTTPClient.delete(urlString, queryMap);
		
		String success = billParser.getMessage(response);
		
		return success;
	}
	
	
//==========================================================================================================================================


	/**
	
	* Return the file attached to the bill.
	
	* Pass the billId to get the attachment of the bill.
	
	* It returns the File object.
	
	
	* @param billId ID of the bill.
	
	* @return Returns the File that is stored in a current directory.
	
	*/
	
	public File getAttachment(String billId)throws Exception
	{
		
		String urlString = url+"/"+billId+"/attachment";  //No I18N
		
		HashMap queryMap = new HashMap();
		
		queryMap.put("authtoken", authToken);
		queryMap.put("organization_id", organizationId);
		
		File file = ZohoHTTPClient.getFile(urlString, queryMap);
		
		return file;
	}	
	
	/**
	
	* Attach a file to a bill.
	
	* Pass the billId and File object to add the attachment of the bill.
	
	* If the file has been attached to the bill it returns the success message.
	
	* The success message is "The document has been attached."
	
	
	* @param billId ID of the bill.
	
	* @param file File to attach.
	
	* @return Returns a String.
	
	*/
	
	public String addAttachment(String billId, File file)throws Exception
	{	
		
		String urlString = url+"/"+billId+"/attachment";  //No I18N
		
		HashMap queryMap = new HashMap();
		
		queryMap.put("authtoken", authToken);
		queryMap.put("organization_id", organizationId);
		
		
		HashMap fileBody = new HashMap();
		
		fileBody.put("attachment", file);
		

		String response = ZohoHTTPClient.post(urlString, queryMap, null, fileBody);

		String success = billParser.getMessage(response);
		
		return success;
	}
	
	/**
	
	* Delete the file attached to a bill.
	
	* Pass the billId to delete the attachment of the bill.
	
	* If the attachment has been deleted it returns the success message.
	
	* The success message is "The attachment has been deleted."
	
	
	* @param billId ID of the bill.
	
	* @return Returns a String.
	
	*/
	
	public String deleteAttachment(String billId)throws Exception
	{
		
		
		String urlString = url+"/"+billId+"/attachment";  //No I18N
		
		HashMap queryMap = new HashMap();
		
		queryMap.put("authtoken", authToken);
		queryMap.put("organization_id", organizationId);
		
		String response = ZohoHTTPClient.delete(urlString, queryMap);
		
		String success = billParser.getMessage(response);
		
		return success;
	}
	
	
//=============================================================================================================================================



	/**
	
	* Get the complete history and comments of a bill.
	
	* Pass the billId to get all the comments for the bill.
	
	* It returns the BillList object.
	 
	
	* @param billId ID of the bill.
	
	* @return Returns the CommentList object.
	
	*/
	
	public CommentList getComments(String billId)throws Exception
	{
		
		String urlString = url+"/"+billId+"/comments";  //No I18N
		
		HashMap queryMap = new HashMap();
		
		queryMap.put("authtoken", authToken);
		queryMap.put("organization_id", organizationId);
		
		String response = ZohoHTTPClient.get(urlString, queryMap);
		
		CommentList commentList = billParser.getComments(response);
		
		return commentList;
	}
	
	/**
	
	* Add a comment for a bill.
	
	* Pass the billId and description to add the comment for the bill.
	
	* It returns the Comment object.
	
	
	* @param billId ID of the bill.
	
	* @param description Description is the comment of the bill.
	
	* @return Returns the Comment object. 
	
	*/
	
	public Comment addComment(String billId, String description)throws Exception
	{
		
		String urlString = url+"/"+billId+"/comments";  //No I18N
		
		HashMap requestBody = new HashMap();
		
		requestBody.put("authtoken", authToken);
		requestBody.put("organization_id", organizationId);
		
		JSONObject jsonObject = new JSONObject();
		
		jsonObject.put("description", description);
		
		requestBody.put("JSONString", jsonObject.toString());
		
		String response = ZohoHTTPClient.post(urlString, requestBody);
		
		Comment comment = billParser.getComment(response);
		
		return comment;
	}
	
	/**
	
	* Delete a bill comment.
	
	* Pass the billId and commentId to delete the comment for the bill.
	
	* If the comment has been deleted it retuens the success message.
	
	* The success message is "The comment has been deleted."
	
	
	* @param billId ID of the bill.
	
	* @param commentId ID of the comment.
	
	* @return Returns a String.
	
	*/
	
	public String deleteComment(String billId, String commentId)throws Exception
	{
		
		String urlString = url+"/"+billId+"/comments"+commentId;  //No I18N
		
		HashMap queryMap = new HashMap();
		
		queryMap.put("authtoken", authToken);
		queryMap.put("organization_id", organizationId);
		
		String response = ZohoHTTPClient.delete(urlString, queryMap);
		
		String success = billParser.getMessage(response);
		
		return success;
	}
	
	
}
