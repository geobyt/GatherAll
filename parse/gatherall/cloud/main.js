var twilio = require("twilio");
twilio.initialize("AC6c64f729f65fdae9c401d57d2cf74cf8","eca66f1bbe7e87a2ebb444d7691ffd94");

var generatePassword = function(length) {
	s = "";
	for (var i = 0; i < length; i++)
	{
		var n = (Math.floor(Math.random() * 100) % 94) + 33;
    	var c = String.fromCharCode(n);
    	s += c;
    }
    return s;
}

// Sign up API
Parse.Cloud.define("inviteWithSMS", function(request, response) {
	checkUserExists(request.params.username).then(function(user) {
		if (user != null) {
			response.error("username already taken");
			return;
		}

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
				Body: "Sign Up Pin: " + pinCode + " at GatherAll App"
			}, {
				success: function(httpResponse) { response.success("SMS sent!"); },
				error: function(httpResponse) { console.log(httpResponse); response.error("Uh oh, something went wrong"); }
			});
		}, function(error) {
			response.error(error);
		});
	});

});

function checkUserExists(username) {
	var userQuery = new Parse.Query(Parse.User);
	userQuery.equalTo("username", username);

	return userQuery.first();
}


Parse.Cloud.define("verifyWithCode", function(request, response) {
	Parse.Cloud.useMasterKey();

	var pinCode = request.params.pinCode;
	var username = request.params.username;

	var password = generatePassword(128);

	var Verification = Parse.Object.extend("Verification");
	var query = new Parse.Query(Verification);
	query.equalTo("username", username);
	query.equalTo("pinCode", pinCode);

	query.first().then(function(verification) {
		if (verification != null) {
			console.log("Pin code verification success");
			
			var userQuery = new Parse.Query(Parse.User);
			userQuery.equalTo("phone", verification.get("phone"))

			userQuery.first().then(function(user) {
				console.log("userQuery results...");
				console.log(user);
				if (user == null) {
					// If user is new, sign up
					console.log("signing up");
					return Parse.User.signUp(username, password, {"phone": verification.get("phone")});
				} else {
					// If user exists, simply update the username and password
					console.log("updating");
					user.set("username", username);
					user.set("password", password);
					return user.save();
				}
			}).then(function() {
				console.log("destroying verification");
				return verification.destroy();	
			}).then(function() {
				console.log("sign up / update success");
				return response.success(password);		
			}).fail(function(err) {
				return response.error(err);
			});

		} else {
			console.log("Error: pin code did not match");
			response.error("Pin code did not match");
			return;
		}
	});
});

// Gathering Invitation
Parse.Cloud.beforeSave("Invitation", function(request, response) {
	var invitation = request.object;

	var query = new Parse.Query(Parse.User);
	query.equalTo("phone", invitation.get('phone'));

	query.first().then(function(user) {
		if (user != null) {
			invitation.set("user", user);

			if (user.get('profilepic') != null) {
				invitation.set("profilepic", user.get('profilepic'));
			}
		}

		response.success();
	});
});

Parse.Cloud.afterSave("Invitation", function(request) {
	var invitation = request.object;

	if (request.object.get("user") != null) {
		// This is an invitation for someone who has this app installed
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
	} else {
		request.object.get("gathering").fetch().then(function(gathering) {
			return gathering.get("owner").fetch();
		}).then(function(owner) {
			twilio.sendSMS({
				From: "+1 802-750-0288",
				To: invitation.get("phone"),
				Body: owner.get('username') + " invited you to a gathering"
			}, {
				success: function(httpResponse) { console.log("SUCCESSFULLY SENT INVITATION") },
				error: function(httpResponse) { console.log(httpResponse); }
			});
		});
	}
});
