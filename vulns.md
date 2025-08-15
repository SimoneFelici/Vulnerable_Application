# Vulnerabilities

## A1:2017-Injection ✅
- SQL Injection:
	- Login form.
	- Ricerca dei frutti.

## A2:2017-Broken Authentication ✅
- Password deboli (admin/admin).
- Unsigned JWT Token.

## A3:2017-Sensitive Data Exposure ✅
- HTTP invece di HTTPS, anche per scambi di credenziali.

## A4:2017-XML External Entities (XXE) ✅
- XEE nella funzionalità "cart".

## A5:2017-Broken Access Control ✅
- CSRF

## A6:2017-Security Misconfiguration ✅
- Directory Listing in /images/.
- Stack Trace nel login.

## A7:2017-Cross-Site Scripting (XSS) ✅
- Reflected XSS nella pagina NOT FOUND.
- Stored XSS nella descrizione della frutta.
- Stored XSS nella carrello della frutta.

## A8:2017-Insecure Deserialization

## A9:2017-Using Components with Known Vulnerabilities ✅
- JQuery (v1.4.2).
- Apache Tomcat (v10.1.1).

## A10:2017-Insufficient Logging & Monitoring ✅
- No logging
