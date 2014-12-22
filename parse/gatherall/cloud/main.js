// User Sign-up
Parse.Cloud.beforeSave(Parse.User, function(request, response) {
	request.object.fetch().then(function(user) {
		if (user == null) {
			console.log("signing up");
		}
	});
})


// Gathering Invitation
Parse.Cloud.beforeSave("Invitation", function(request, response) {
	var invitation = request.object;

	var query = new Parse.Query(Parse.User);
	query.equalTo("phonenumber", invitation.get('phonenumber'));

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
