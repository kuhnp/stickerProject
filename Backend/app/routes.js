// app/routes.js
var User            = require('../app/models/user');
var jwt        = require("../node_modules/jsonwebtoken");

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



function ensureAuthorized(req, res, next) {
    console.log('Entered in authorized function');
    var bearerToken;    
    var bearerHeader = req.headers["token"];
    if (typeof bearerHeader !== 'undefined') {
        var bearer = bearerHeader.split(" ");
        req.token = bearer;
        next();
    } else {
        res.send(403);
    }
}

};

// route middleware to make sure a user is logged in
function isLoggedIn(req, res, next) {

    // if user is authenticated in the session, carry on 
    if (req.isAuthenticated())
        return next();

    // if they aren't redirect them to the home page
    res.redirect('/');
}