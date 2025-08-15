# Vulnerabilities

## A1:2017-Injection
- SQL Injection:
	- Login form;
	- Fruit search.

## A2:2017-Broken Authentication
- Weak credentials (admin/admin);
- Unsigned JWT Token.

## A3:2017-Sensitive Data Exposure
- HTTP instead of HTTPS, also while logging in;
- GET Login.

## A4:2017-XML External Entities (XXE)
- XEE in the "cart" functionality.

## A5:2017-Broken Access Control
- CSRF.

## A6:2017-Security Misconfiguration
- Directory Listing in /images/;
- Stack Trace nel login.

## A7:2017-Cross-Site Scripting (XSS)
- DOM XSS in the "NOT FOUND" page;
- Stored XSS in the fruit description;
- Stored XSS in the fruit cart.

## A8:2017-Insecure Deserialization

## A9:2017-Using Components with Known Vulnerabilities
- JQuery (v1.4.2);
- Apache Tomcat (v10.1.1).

## A10:2017-Insufficient Logging & Monitoring
- No logging.
