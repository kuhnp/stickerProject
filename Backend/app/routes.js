// app/routes.js
var User            = require('../app/models/user');
var Friend          = require('../app/models/friend');
var jwt             = require("../node_modules/jsonwebtoken");
var multer          = require('../node_modules/multer');
var randomstring    = require("randomstring");
var gcm             = require('node-gcm');
var fs              = require('fs');

var usersController     = require('./Controllers/users-controller');
var friendsController   = require('./Controllers/friend-controller');
var stickersController   = require('./Controllers/sticker-controller');

module.exports = function(app, passport) {

    app.get('/', function(req, res) {
        res.json({
            data: "Hello World"
        });
    });

    app.post('/signup', usersController.signup);

    app.post('/login', usersController.signin);

    app.post('/friend',ensureAuthorized, friendsController.add);

    app.post('/friendaccept', ensureAuthorized, friendsController.accept);

    app.get('/friend', ensureAuthorized, friendsController.getfriends);



app.post('/sticker', 
    ensureAuthorized, 
    multer(
            {
                dest: './uploads/', rename: function (fieldname, filename) 
                {
                    return filename.replace(/\W+/g, '-').toLowerCase();
                }
            }
    ), stickersController.post);



app.get('/sticker', ensureAuthorized, stickersController.get);


app.get('/sticker/:user_id', ensureAuthorized, stickersController.getbyid);


app.post('/notificationId', ensureAuthorized, usersController.notificationid);


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