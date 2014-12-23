var twilio = require("twilio");
twilio.initialize("AC6c64f729f65fdae9c401d57d2cf74cf8","eca66f1bbe7e87a2ebb444d7691ffd94");
 
// Sign up API
Parse.Cloud.define("inviteWithSMS", function(request, response) {
	var pinCodeNumber = Math.floor(Math.random() * 10000);
	var pinCode = ('0000' + pinCodeNumber).slice(-4);

	var verification = new Parse.Object("Verification");

	verification.set("username", request.params.username);
	verification.set("pinCode", pinCode);
	verification.set("phone", request.params.phone);

	verification.save().then(function(results) {
		console.log("verification saved");

		// Use the Twilio Cloud Module to send an SMS
		twilio.sendSMS({
			From: "+1 802-750-0288",
			To: request.params.phone,
			Body: "Login with pinCode: " + pinCode
		}, {
			success: function(httpResponse) { response.success("SMS sent!"); },
			error: function(httpResponse) { console.log(httpResponse); response.error("Uh oh, something went wrong"); }
		});
	}, function(error) {
		response.error(error);
	})
});

Parse.Cloud.define("verifyWithCode", function(request, response) {
	var pinCode = request.params.pinCode;
	var username = request.params.username;

	var password = "hihihiih";

	var Verification = Parse.Object.extend("Verification");
	var query = new Parse.Query(Verification);
	query.equalTo("username", username);
	query.equalTo("pinCode", pinCode);

	query.first().then(function(verification) {
		if (verification != null) {
			console.log("successfully retrieved the verification");
			return Parse.User.signUp(username, password, {"phone": verification.get("phone")});
		} else {
			console.log("ERROR");
			response.error("Pin code did not match");
			return;
		}
	}).then(function() {
		console.log("SUCCESSFULLY signed up and passed back password");
		response.success(password);
		return;
	});
});

// Gathering Invitation
Parse.Cloud.beforeSave("Invitation", function(request, response) {
	var invitation = request.object;

	var query = new Parse.Query(Parse.User);
	query.equalTo("phone", invitation.get('phone'));

	query.first().then(function(user) {
		invitation.set("user", user);
		invitation.set("profilepic", user.get('profilepic'));
		response.success();
	});
});

Parse.Cloud.afterSave("Invitation", function(request) {
	var invitation = request.object;

	request.object.get("user").fetch().then(function(user) {
		request.object.get("gathering").fetch().then(function(gathering) {
			return gathering.get("owner").fetch();
		}).then(function(owner) {
			var installationQuery = new Parse.Query(Parse.Installation);
			installationQuery.equalTo("user", user);
			
			console.log("Sending to " + user.get("username") + " msg: " + owner.get("username") + " invited you to a gathering");
			Parse.Push.send({
				where: installationQuery,
				data: {
					alert: owner.get('username') + " invited you to a gathering"
				}
			});
		});
		
	});
});
