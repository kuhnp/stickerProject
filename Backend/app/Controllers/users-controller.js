var User            = require('./../models/user');
var jwt             = require("./../../node_modules/jsonwebtoken");



module.exports.signin = function(req, res){
  User.findOne({'email': req.body.email}, function(err, user) {
    if (err) {
      res.json({
        type: false,
        data: "Error occured: " + err
      });
    } else {
      if (user) {
        console.log('User exists...');
        if (!user.validPassword(req.body.password)){
          res.json({
            type: false,
            data: 'Incorrect Password or username'
          });
          console.log('Incorrect PW');
        } 
        else {
          console.log('User found');
          res.json({
            type: true,
            token: user.token
          });    
        }
      } else {
        console.log('User not registered');
        res.json({
          type: false,
          data: "Incorrect Password or username"
        });    
      }
    }
  });
}

module.exports.signup = function(req, res){
  User.findOne({'email': req.body.email}, function(err, user) {
    if (err) {
      res.json({
        type: false,
        data: "Error occured: " + err,
        errorCode : 0
      });
            } else {            // the email address is already registered
              if (user) {
                res.json({
                  type: false,
                  data: 'email already exists',
                  errorCode : 1
                }); 
              } 
             else {           // new email address

              User.findOne({'username': req.body.username}, function(err, user2) {
                if(user2)
                    {          // if username is already taken
                      res.json({
                        type: false,
                        data: "username already exists",
                        errorCode : 2
                      });
                    }
                    else
                    {
                      var newUser = new User();
                      newUser.email = req.body.email;
                      newUser.password = newUser.generateHash(req.body.password);
                      newUser.username = req.body.username;
                      console.log('Creating new user token...');
                      newUser.save(function(err,user){
                        user.token = jwt.sign(newUser, process.env.JWT_SECRET);
                        var decode = jwt.verify(user.token,process.env.JWT_SECRET);
                        console.log('decode.email = '+decode.email);
                        console.log('Done');
                        user.save(function(err,user1){
                          res.json({ 
                            data: 'user authenticated',
                            type: true,
                            token : user1.token
                          });
                        });
                      });
                    }
                });
      }
    }
  });
}

module.exports.notificationid = function(req, res) {
  var decode = jwt.verify(req.token,process.env.JWT_SECRET);
  var username = decode.username;
  console.log('username = '+username);
  User.findOne({'username': username},function(err,user){
    if (err) {
      res.json({
        type: false,
        data: "Error occured: " + err,
        errorCode : 0
      });
    }
    else {
      if(user){
        user.regId = req.body.registration_id;
        console.log('user:'+user.username+' is now registered with registration id:'+req.body.registration_id);
        user.save(function(err,usr){
          res.json({
            type: true,
            data: 'regId saved in the database'
          });
        });
      }
      else {
        console.log('User not registered');
        res.json({
          type: false,
          data: "User not found"
        });    
      }
    }
  }); 
}
