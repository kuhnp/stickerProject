// server.js

// BASE SETUP
// =============================================================================

// call the packages we need
var express    = require('express');        // call express
var app        = express();                 // define our app using express
var bodyParser = require('body-parser');
var mongoose   = require('mongoose');
var multer     = require('multer');
var passport   = require('passport');
var flash      = require('connect-flash');
var morgan       = require('morgan');
var cookieParser = require('cookie-parser');
var session      = require('express-session');
var configDB = require('./config/database.js');

mongoose.connect('mongodb://localhost/stickerApp'); // connect to our database

// configuration ===============================================================
//mongoose.connect(configDB.url); // connect to our database




app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser());  

app.use(morgan('dev')); // log every request to the console
app.use(cookieParser()); // read cookies (needed for auth)

app.use(express.static(__dirname + '/static'));
app.set('view engine', 'ejs'); // set up ejs for templating



// routes ======================================================================
require('./app/routes.js')(app); // load our routes and pass in our app and fully configured passport



var port = process.env.PORT || 8080;        // set our port



// START THE SERVER
// =============================================================================
app.listen(port);
console.log('Magic happens on port ' + port);
