var User            = require('./../models/user');
var Friend          = require('./../models/friend');
var jwt             = require("./../../node_modules/jsonwebtoken");
var gcm             = require('node-gcm');
var fs              = require('fs');
var config			= require('./../../../../config');





module.exports.post = function(req, res) {
	var decode = jwt.verify(req.token,process.env.JWT_SECRET);
	var dest = req.headers["dest"];
	var username = decode.username;

	var message = new gcm.Message();
	message.addData('messageType', 'postSticker');
	message.addData('senderUsername',username);
	var regIds ;

	User.findOne({'username': dest }, function(err,usr){
		if (err) {
			res.json({
				type: false,
				data: "Error occured: " + err,
				errorCode : 0
			});
		}
		else {
			if(usr){
				regIds = usr.regId;
				console.log('Dest is found, regId = '+regIds);
				var sender = new gcm.Sender(config.gcm.senderId);
				sender.send(message, regIds, function (err, result) {
					if(err) 
						console.log('error occured when sending');
					else    
						console.log(result);
				});
				res.json({
					type:true,
				});
			}
			else{
				console.log('User not registered');
				res.json({
					type: false,
					data: "User not found"
				});
			}
		}    
	});
}

module.exports.get = function(req, res){
	console.log('sticker request received');
	var decode = jwt.verify(req.token,process.env.JWT_SECRET);
	var username = decode.username;
	console.log('sticker request for '+username);
	if(fs.existsSync('/Users/pierre/Documents/Git/stickerProject/Backend/uploads/'+username+'.jpg'))
		res.sendfile('/Users/pierre/Documents/Git/stickerProject/Backend/uploads/'+username+'.jpg');
	else{
		console.log('Send default picture');
		res.sendfile('/Users/pierre/Documents/Git/stickerProject/Backend/uploads/defaultimage.jpg');
	}
}

module.exports.getbyid = function(req, res){
	console.log('sticker request received');
    var decode = jwt.verify(req.token,process.env.JWT_SECRET);
    var username = decode.username;
    console.log('sticker request for '+req.params.user_id);

    if(fs.existsSync('/Users/pierre/Documents/Git/stickerProject/Backend/uploads/'+req.params.user_id+'.jpg')){
        res.sendfile('/Users/pierre/Documents/Git/stickerProject/Backend/uploads/'+req.params.user_id+'.jpg');
    }
    else{
        console.log('Send default picture');
        res.sendfile('/Users/pierre/Documents/Git/stickerProject/Backend/uploads/defaultimage.jpg');
    }
}


