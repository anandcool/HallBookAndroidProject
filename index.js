const express = require("express");
const mysql = require("mysql");

//Connect to MySQL
var con = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "root",
  database: "android_nodejs",
});

con.connect((err) => {
  if (err) throw err;
  console.log("MYSQL is connected");
});

//Middlewares
var app = express();
app.use(express.json()); //accept JSON params
app.use(express.urlencoded({ extended: true })); //accept URL encoded params

//Signup Handle
app.post("/signup", (req, res) => {
  var postData = req.body;
  var name = postData.name;
  var email_id = postData.email_id;
  var password = postData.password;
  var confirm_pass = postData.confirm_pass;
  var phone_no = postData.phone_no;

  con.query(
    "SELECT * FROM users where email_id=?",
    [email_id],
    (err, result) => {
      if (error) {
        console.log(test.sql);
        return res.status(400).json({
          error: "MYSQL ERROR",
        });
      }
      if (result && result.length) res.send("Email-id is already Registered");
      else {
        let sql = "INSERT INTO `users` SET ?";
        let data = { name, email_id, password, confirm_pass, phone_no };
        let test = con.query(sql, data, (err, result) => {
          if (error) {
            console.log(test.sql);
            return res.status(400).json({
              error: "MYSQL ERROR",
            });
          }
          console.log(test.sql);
          res.send("User Registered Successfully");
        });
      }
    }
  );
});

//Login Handle
app.post("/login", (req, res) => {
  var postData = req.body;
  var email_id = postData.email_id;
  var password = postData.password;
  var finalResult = {};
  con.query(
    "SELECT * FROM users where email_id=?",
    [email_id],
    (error, result) => {
      if (error) {
        console.log(test.sql);
        return res.status(400).json({
          error: "MYSQL ERROR",
        });
      }
      if (result && result.length) {
        if (password === result[0].password) {
          finalResult.error = false;
          finalResult.message = "Login Success";
          finalResult.user = result[0];
          res.send(finalResult);
          console.log(finalResult);
        } else {
          finalResult.error = true;
          finalResult.message = "Login Failed";
          res.send(finalResult);
          console.log(finalResult);
        }
      } else {
        finalResult.error = true;
        finalResult.message = "Invalid email or password";
        res.send(finalResult);
        console.log(finalResult);
      }
    }
  );
});

//city list
app.get("/cities", (req, res) => {
  let sql = "SELECT DISTINCT(city) from halls";
  let test = con.query(sql, (error, result) => {
    if (error) {
      console.log(test.sql);
      return res.status(400).json({
        error: "MYSQL ERROR",
      });
    }
    console.log(result);
    res.json(result);
  });
});

//City Value
app.post("/city", (req, res) => {
  var city_name = req.body.city_name;
  var email_id = req.body.email_id;
  let sql = "UPDATE users SET ? where ?";
  let data = [{ city_name }, { email_id }];
  let test = con.query(sql, data, (error, result) => {
    if (error) {
      console.log(test.sql);
      return res.status(400).json({
        error: "CITY value can't be SET into db",
      });
    }
    console.log(test.sql);
    res.send("City name updated");
  });
});

//Halls in a particular city
app.post("/halls", (req, res) => {
  var city = req.body.city;
  let sql = "Select id, name, rating from halls where ?";
  let data = { city };
  var finalResult = [];
  let test = con.query(sql, data, (error, result) => {
    if (error) {
      console.log(test.sql);
      return res.status(400).json({
        error: "MYSQL ERROR",
      });
    }
    if (result && result.length) {
      finalResult = result;
      res.send(finalResult);
      console.log(finalResult);
    } else {
      res.send("No halls found for this particular location");
    }
    console.log(test.sql);
  });
});

//Details of a particular hall
app.post("/hall", (req, res) => {
  var id = req.body.id;
  let sql = "Select * from halls where id ='" + id + "' ";
  let test = con.query(sql, (error, result) => {
    if (error) {
      console.log(test.sql);
      return res.status(400).json({
        error: "MYSQL ERROR",
      });
    }
    if (result && result.length) {
      finalResult = result;
      res.send(finalResult);
      console.log(finalResult);
    } else {
      res.send("No halls found for this particular location");
    }
    console.log(test.sql);
  });
});

//to return all the booked dates for this particular hall
app.post("/bookedDates", (req, res) => {
  var hall_id = req.body.hall_id;

  let sql = "SELECT start_date, end_date from bookings where ?";
  let data = { hall_id };
  let test = con.query(sql, data, (error, result) => {
    if (error) {
      console.log(test.sql);
      return res.status(400).json({
        error: "MYSQL ERROR",
      });
    }
    console.log(result);
    return res.status(200).json(result);
  });
});

//get details of user to edit profile
app.post("/user/details", (req, res) => {
  var email_id = req.body.email_id;
  let sql = "SELECT name, password, confirm_pass, phone_no from users where ?";
  let data = { email_id };
  let test = con.query(sql, data, (error, result) => {
    if (error) {
      console.log(test.sql);
      return res.json({
        error: "MYSQL error",
      });
    }
    console.log(test.sql);
    console.log(result);
    res.json(result);
  });
});

//to update the details entered by user
app.post("/user/update/details", (req, res) => {
  var name = req.body.name;
  var password = req.body.password;
  var confirm_pass = req.body.confirm_pass;
  var phone_no = req.body.phone_no;
  var email_id = req.body.email_id;

  let sql = "UPDATE users SET ? where ?";
  let data = [{ name, password, confirm_pass, phone_no }, { email_id }];
  let test = con.query(sql, data, (error, result) => {
    if (error) {
      console.log(test.sql);
      return res.status(400).json({
        error: "User details cannot be UPDATED",
      });
    }
    console.log(test.sql);
    console.log(result);
    res.send("updated");
  });
});

//to save a new booking to db
app.post("/book/hall", (req, res) => {
  var customer_id = req.body.customer_id;
  var hall_id = req.body.hall_id;
  var occasion = req.body.occasion;
  var start_date = req.body.start_date;
  var end_date = req.body.end_date;
  var guests_no = req.body.guests_no;
  var amount = req.body.amount;

  let sql = "INSERT INTO bookings SET ?";
  let data = {
    customer_id,
    hall_id,
    occasion,
    start_date,
    end_date,
    guests_no,
    amount,
  };
  let test = con.query(sql, data, (error, result) => {
    if (error) {
      console.log(test.sql);
      return res.status(400).json({
        error: "Booking failed",
      });
    }
    console.log(test.sql);
    console.log(result);
    res.send("booked");
  });
});

//Start Server
const PORT = process.env.PORT || 9000;
app.listen(PORT, () => {
  console.log(`Application server running on port ${PORT}`);
});
