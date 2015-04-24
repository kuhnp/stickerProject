var User            = require('./../models/user');
var Friend          = require('./../models/friend');
var jwt             = require('jsonwebtoken');
var gcm             = require('node-gcm');
var config      = require('./../../../../config');


module.exports.add = function(req, res) {
  var decode = jwt.verify(req.token,process.env.JWT_SECRET);
  var username = decode.username;

  User.findOne({'username':req.body.friend}, function(err,user){
    if(err){
      res.json({
        type: false,
        errorCode: 0
      });
    }
    else{
      if(user){
        Friend.findOne(
        {
          $or:   
          [
            {'user1':req.body.friend, 'user2':username},
            {'user2':req.body.friend, 'user1':username}
          ]

        }, function (err,friend){
          if(friend){
            console.log("Error Already friend with");
            res.json({
              type: false,
              errorCode: 4,
              message: "Already friend with"
            });   
          }
          else{
            console.log("Friend found");
            var newFriend = new Friend();
            newFriend.user1 = username;
            newFriend.user2 = req.body.friend;
            newFriend.isFriend = 'false';


            var message = new gcm.Message();
            var sender = new gcm.Sender(config.gcm.senderId);
            message.addData({
              messageType: 'friendRequest',
              senderUsername: username,
              username: req.body.friend                    
                            });
                            var regId = user.regId;
                            console.log('before sending, regId = '+regId);
                            sender.send(message, regId, function (err, result) {
                              if(err) 
                                console.log('error occured when sending');
                              else    
                                console.log('message sent to '+req.body.friend);
                            });
                            newFriend.save(function(err,friend2){
                              res.json({
                                type: true,
                                message: "New friendship"
                              });
                            });
                        }
                    });
      }
      else {
        console.log("Error User does not exist");
        res.json({
          type: false,
          errorCode: 3,
          data: "User does not exist"
        });
      }
    }
  });
}


module.exports.accept = function(req, res) {
    var decode = jwt.verify(req.token,process.env.JWT_SECRET);
    var username = decode.username;
    Friend.findOne(
    {
      $or:   
      [
        {'user1':req.body.friend, 'user2':username},
        {'user2':req.body.friend, 'user1':username}
      ]
    }, function(err,friend){
        if(friend){
            console.log('Friendship found');
            friend.isFriend = 'true';
            friend.save(function(err,friendSaved){
                var message = new gcm.Message();
                var sender = new gcm.Sender(config.gcm.senderId);
                message.addData({
                    messageType: 'friendRequestAccepted',
                    senderUsername: username,
                    username: req.body.friend
                });
                User.findOne({'username':req.body.friend}, function(err2,user){
                    if(err2){
                        res.json({
                            type: false,
                            errorCode: 0
                        });
                    }
                    else{
                        if(user){
                            console.log('regId = '+user.regId);
                            var regId = user.regId;
                            sender.send(message, regId, function (err, result) {
                            if(err) 
                                console.log('error occured when sending');
                            else    
                                console.log('message sent to '+req.body.friend);
                            });

                            res.json({
                            type: true
                            });
                        }
                        else
                            console.log(''+user.regId+' not found');
                    }
                });   
            });
        }
    });
}

module.exports.getfriends = function(req, res){
  var decode = jwt.verify(req.token,process.env.JWT_SECRET);
  var username = decode.username;
  console.log(username);
  var i = 0;
  var jsonObj = JSON.parse('{"type": true, "friendNum":0, "friends": []}');;

  Friend.find({},function(err,friends){
    if(err){
      res.json({
        type: false,
        errorCode: 0
      });
    }
    else{  
      friends.forEach(function(frien){
        if(frien.user1 == username){
          console.log("Friend with "+frien.user2);
          jsonObj.friends[i] = JSON.parse('{"username":"'+frien.user2+'", "isFriend":"'+frien.isFriend+'", "fromUser":"true"}');
          i++;
        }
        else if(frien.user2 == username){
          console.log("Friend with "+frien.user1);
          jsonObj.friends[i] = JSON.parse('{"username":"'+frien.user1+'", "isFriend":"'+frien.isFriend+'", "fromUser":"false"}');
          i++;
        }
      });
      var friendNumber = i;
      jsonObj.friendNum = JSON.parse(friendNumber);
      res.json(jsonObj);
    }
  });
}