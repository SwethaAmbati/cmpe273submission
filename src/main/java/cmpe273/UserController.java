package cmpe273;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@RestController
public class UserController {

	private static final Map<String, User> userInfo = new HashMap<String, User>();
	private static final Map<String, Map<String, IDCard>> cardInfo = new HashMap<String, Map<String, IDCard>>();
	private static final Map<String, Map<String, WebLogin>> loginInfo = new HashMap<String, Map<String, WebLogin>>();
	private static final Map<String, Map<String, BankAccount>> bankInfo = new HashMap<String, Map<String, BankAccount>>();

	Random random = new Random();

	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	// Date date = new Date();
	Calendar calobj = Calendar.getInstance();
	Gson gson = new Gson();

	// create user using POST
	@RequestMapping(value = "api/v1/users/create", method = RequestMethod.POST)
	public ResponseEntity<String> createUser(@Valid @RequestBody User user,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

		// int number = random.nextInt(1000);
		user.setUserId("u-" + random.nextInt(1000));

		user.setCreated_at(dateFormat.format(calobj.getTime()));
		userInfo.put(user.getUserId(), user);

		JsonObject jObject = new JsonObject();
		jObject.addProperty("user_id", user.getUserId());
		jObject.addProperty("email", user.getEmail());
		jObject.addProperty("password", user.getPassword());
		jObject.addProperty("created_at", user.getCreated_at());

		return new ResponseEntity<String>(gson.toJson(jObject),
				HttpStatus.CREATED);

	}

	// view user details using GET

	@RequestMapping(value = "api/v1/users/{user_id}", method = RequestMethod.GET)
	public ResponseEntity<String> viewUser(@PathVariable String user_id) {

		User user = userInfo.get(user_id);

		JsonObject jObject = new JsonObject();
		jObject.addProperty("user_id", user.getUserId());
		jObject.addProperty("email", user.getEmail());
		jObject.addProperty("password", user.getPassword());
		jObject.addProperty("created_at", user.getCreated_at());
		return new ResponseEntity<String>(gson.toJson(jObject), HttpStatus.OK);

	}

	// update a specific user using PUT

	@RequestMapping(value = "api/v1/users/{user_id}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateUser(@PathVariable String user_id,
			@Valid @RequestBody User user, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

		userInfo.get(user_id).setEmail(user.getEmail());
		userInfo.get(user_id).setPassword(user.getPassword());
		userInfo.get(user_id)
				.setUpdated_at(dateFormat.format(calobj.getTime()));

		JsonObject jObject = new JsonObject();
		User userObj = userInfo.get(user_id);
		jObject.addProperty("user_id", userObj.getUserId());
		jObject.addProperty("email", userObj.getEmail());
		jObject.addProperty("password", userObj.getPassword());
		jObject.addProperty("updated_at", userObj.getUpdated_at());
		return new ResponseEntity<String>(gson.toJson(jObject),
				HttpStatus.CREATED);

	}

	// IDCARD

	// create id card

	@RequestMapping(value = "api/v1/users/{user_id}/idcards", method = RequestMethod.POST)
	public ResponseEntity<IDCard> createIdCard(
			@Valid @RequestBody IDCard idcard, BindingResult result,
			@PathVariable String user_id) {

		if (result.hasErrors()) {
			return new ResponseEntity<IDCard>(HttpStatus.BAD_REQUEST);
		}

		idcard.setCard_id("c-" + random.nextInt(1000));

		if (cardInfo.get(user_id) == null) {
			Map<String, IDCard> idcardmap = new HashMap<String, IDCard>();
			idcardmap.put(idcard.getCard_id(), idcard);
			cardInfo.put(user_id, idcardmap);
		} else {
			cardInfo.get(user_id).put(idcard.getCard_id(), idcard);
		}

		return new ResponseEntity<IDCard>(idcard, HttpStatus.CREATED);
	}

	// list all id cards

	@RequestMapping(value = "api/v1/users/{user_id}/idcards", method = RequestMethod.GET)
	public ResponseEntity<String> listIdCard(@PathVariable String user_id) {

		Map<String, IDCard> idCard = cardInfo.get(user_id);
		String jsonStr = gson.toJson(idCard.values());
		return new ResponseEntity<String>(jsonStr, HttpStatus.OK);

	}

	// Delete id card

	@RequestMapping(value = "api/v1/users/{user_id}/idcards/{card_id}", method = RequestMethod.DELETE)
	public ResponseEntity<IDCard> deleteIdCard(@PathVariable String card_id,
			@PathVariable String user_id) {

		cardInfo.get(user_id).remove(card_id);
		return new ResponseEntity<IDCard>(HttpStatus.NO_CONTENT);
	}

	// WEB LOGIN

	// Create Web Login

	@RequestMapping(value = "api/v1/users/{user_id}/weblogins", method = RequestMethod.POST)
	public ResponseEntity<WebLogin> createWebLogin(
			@Valid @RequestBody WebLogin weblogin, BindingResult result,
			@PathVariable String user_id) {

		if (result.hasErrors()) {
			return new ResponseEntity<WebLogin>(HttpStatus.BAD_REQUEST);
		}
		weblogin.setLogin_id("l-" + random.nextInt(1000));

		if (loginInfo.get(user_id) == null) {
			Map<String, WebLogin> webloginmap = new HashMap<String, WebLogin>();
			webloginmap.put(weblogin.getLogin_id(), weblogin);
			loginInfo.put(user_id, webloginmap);
		} else {
			loginInfo.get(user_id).put(weblogin.getLogin_id(), weblogin);
		}

		return new ResponseEntity<WebLogin>(weblogin, HttpStatus.CREATED);
	}

	// list all web logins

	@RequestMapping(value = "api/v1/users/{user_id}/weblogins", method = RequestMethod.GET)
	public ResponseEntity<String> listWebLogin(@PathVariable String user_id) {

		Map<String, WebLogin> weblogin = loginInfo.get(user_id);
		String jsonStr = gson.toJson(weblogin.values());
		return new ResponseEntity<String>(jsonStr, HttpStatus.OK);

	}

	// Delete Web Login

	@RequestMapping(value = "api/v1/users/{user_id}/weblogins/{login_id}", method = RequestMethod.DELETE)
	public ResponseEntity<WebLogin> deleteWebLogin(
			@PathVariable String login_id, @PathVariable String user_id) {

		loginInfo.get(user_id).remove(login_id);
		return new ResponseEntity<WebLogin>(HttpStatus.NO_CONTENT);
	}

	// BANK ACCOUNT

	// Create Bank Account

	@RequestMapping(value = "api/v1/users/{user_id}/bankaccounts", method = RequestMethod.POST)
	public ResponseEntity<BankAccount> createBankAccount(
			@Valid @RequestBody BankAccount bankaccount, BindingResult result,
			@PathVariable String user_id) {

		if (result.hasErrors()) {
			return new ResponseEntity<BankAccount>(HttpStatus.BAD_REQUEST);
		}
		bankaccount.setBa_id("b-" + random.nextInt(1000));

		if (bankInfo.get(user_id) == null) {
			Map<String, BankAccount> bankmap = new HashMap<String, BankAccount>();
			bankmap.put(bankaccount.getBa_id(), bankaccount);
			bankInfo.put(user_id, bankmap);
		} else {
			bankInfo.get(user_id).put(bankaccount.getBa_id(), bankaccount);
		}

		return new ResponseEntity<BankAccount>(bankaccount, HttpStatus.OK);
	}

	// list all bank accounts
	@RequestMapping(value = "api/v1/users/{user_id}/bankaccounts", method = RequestMethod.GET)
	public ResponseEntity<String> listBankAccount(@PathVariable String user_id) {
		Map<String, BankAccount> bankaccount = bankInfo.get(user_id);
		String jsonStr = gson.toJson(bankaccount.values());

		return new ResponseEntity<String>(jsonStr, HttpStatus.OK);
	}

	// Delete Bank Account

	@RequestMapping(value = "api/v1/users/{user_id}/bankaccounts/{ba_id}", method = RequestMethod.DELETE)
	public ResponseEntity<BankAccount> deleteBankAccount(
			@PathVariable String ba_id, @PathVariable String user_id) {

		bankInfo.get(user_id).remove(ba_id);
		return new ResponseEntity<BankAccount>(HttpStatus.NO_CONTENT);

	}

}
