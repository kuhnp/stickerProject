// app/routes.js
var User            = require('../app/models/user');
var Friend          = require('../app/models/friend');
var jwt             = require("../node_modules/jsonwebtoken");
var multer          = require('../node_modules/multer');
var randomstring = require("randomstring");

module.exports = function(app, passport) {

    // =====================================
    // HOME PAGE (with login links) ========
    // =====================================
    app.get('/', function(req, res) {
        res.json({
            data: "Hello World"
        });
    });

    
    // show the login form
    app.get('/login', function(req, res) {

        // render the page and pass in any flash data if it exists
        res.render('login.ejs', { message: req.flash('loginMessage') }); 
    });


    // =====================================
    // SIGNUP ==============================
    // =====================================

    // process the signup form
    // app.post('/signup', do all our passport stuff here);

    app.post('/signup', function(req, res) {
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
                        newUser.firstname = req.body.firstname;
                        newUser.lastname = req.body.lastname;
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
});

    // =====================================
    // LOGIN ===============================
    // =====================================

    app.post('/login', function(req, res) {
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
    });



    app.get('/me', ensureAuthorized, function(req, res) {
        console.log('Received GET');
        console.log('2token='+req.token);
    User.findOne({'token': req.token}, function(err, user) {
        if (err) {
            res.json({
                type: false,
                data: "Error occured: " + err
            });
        } else {
            if(user){
            res.json({
                type: true,
                data: user
            });}
            else{
                res.json({
                type: false
            });
            }
        }
    });
});


app.post('/friend',ensureAuthorized,function(req, res) {
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
                console.log("Friend found");
                Friend.findOne(
                {
                    $or:   
                    [
                        {'user1':req.body.friend, 'user2':username},
                        {'user2':req.body.friend, 'user1':username}
                    ]

                }, function (err,friend){
                        console.log("Friend found2");
                        if(friend){
                            res.json({
                            type: false,
                            errorCode: 4,
                            message: "Already friend with"
                            });   
                        }
                        else{
                            console.log("Friend found3");
                            var newFriend = new Friend();
                            newFriend.user1 = username;
                            newFriend.user2 = req.body.friend;
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
                res.json({
                type: false,
                errorCode: 3,
                data: "User does not exist"
                });
            }
        }
    });
});




app.get('/friend', ensureAuthorized, function(req,res){     //get all the friends
    var decode = jwt.verify(req.token,process.env.JWT_SECRET);
    var username = decode.username;
    console.log(username);
    var i = 0;
    var jsonObj = JSON.parse('{"type": true, "friendNum":0, "friends": []}');;
    console.log('before cursor');

    Friend.find({},function(err,friends){
        if(err){
            res.json({
                type: false,
                errorCode: 0
            });
        }
        else{  friends.forEach(function(frien){
                if(frien.user1 == username){
                    jsonObj.friends[i] = JSON.parse('{"username":"'+frien.user2+'"}');
                    i++;
                }
                else if(frien.user2 == username){
                    console.log(frien);
                    jsonObj.friends[i] = JSON.parse('{"username":"'+frien.user1+'"}');
                    i++;
                }
            });
            var friendNumber = i;
            jsonObj.friendNum = JSON.parse(friendNumber);
            console.log(jsonObj.friends[0].username);
            res.json(jsonObj);
        }
    });
});



app.post('/sticker', ensureAuthorized, multer({dest: './uploads/', rename: function (fieldname, filename) {
    return filename.replace(/\W+/g, '-').toLowerCase();
  }}), function(req,res){
    var decode = jwt.verify(req.token,process.env.JWT_SECRET);
    var dest = req.body.dest;
    var username = decode.username;
    
    var imagePath =  "/Users/pierre/Documents/Git/stickerProject/Backend/uploads/"+dest+".JPG";

    res.json({
                type:true,
                url: imagePath
            });

});


app.get('/sticker', ensureAuthorized,function(req,res){
    var decode = jwt.verify(req.token,process.env.JWT_SECRET);
    var username = decode.username;

    res.json({
                message:true,
                imageUrl: "/Users/pierre/Documents/Git/stickerProject/Backend/uploads/"+username+".JPG"
            });

});

function ensureAuthorized(req, res, next) {
    console.log('Entered in authorized function');
    var bearerToken;    
    var bearerHeader = req.headers["token"];
    if (typeof bearerHeader !== 'undefined') {
        req.token = bearerHeader;
        next();
    } else {
        res.send(403);
    }
}

};

function getExtension(fn) {
    return fn.split('.').pop();
}

// route middleware to make sure a user is logged in
function isLoggedIn(req, res, next) {

    // if user is authenticated in the session, carry on 
    if (req.isAuthenticated())
        return next();

    // if they aren't redirect them to the home page
    res.redirect('/');
}




//find({$or:[{'user1':piero},{'user2':piero}]})