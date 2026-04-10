import urllib.request
import urllib.error
import json

base_url = "http://localhost:8080"

users = {
    "admin": {"email": "admin@example.com", "password": "admin123", "role": "ROLE_ADMIN"},
    "analyst": {"email": "analyst@example.com", "password": "analyst123", "role": "ROLE_ANALYST"},
    "viewer": {"email": "viewer@example.com", "password": "viewer123", "role": "ROLE_VIEWER"}
}

endpoints = {
    "get_summary": {"method": "GET", "path": "/api/finance/summary"},
    "get_transactions": {"method": "GET", "path": "/api/finance/transactions"},
    "get_audit_logs": {"method": "GET", "path": "/api/admin/audit-logs"},
    "create_transaction": {"method": "POST", "path": "/api/finance/transactions", "body": {"amount": 100, "type": "EXPENSE", "category": "Test", "date": "2026-04-10", "notes": "Test"}},
    "create_budget": {"method": "POST", "path": "/api/budgets", "body": {"categoryName": "Food", "budgetAmount": 500.00}}
}

def login(email, password):
    req = urllib.request.Request(f"{base_url}/api/auth/signin", method="POST")
    req.add_header("Content-Type", "application/json")
    data = json.dumps({"email": email, "password": password}).encode("utf-8")
    try:
        response = urllib.request.urlopen(req, data=data)
        res_data = json.loads(response.read().decode("utf-8"))
        return res_data.get("data", {}).get("token")
    except Exception as e:
        print(f"Login failed for {email}: {e}")
        return None

def test_endpoint(token, endpoint_def):
    req = urllib.request.Request(f"{base_url}{endpoint_def['path']}", method=endpoint_def["method"])
    req.add_header("Authorization", f"Bearer {token}")
    
    if "body" in endpoint_def:
        req.add_header("Content-Type", "application/json")
        data = json.dumps(endpoint_def["body"]).encode("utf-8")
        req.data = data
        
    try:
        response = urllib.request.urlopen(req)
        return response.getcode()
    except urllib.error.HTTPError as e:
        return e.code
    except Exception as e:
        print(e)
        return -1

for name, creds in users.items():
    print(f"\n--- Testing for {name.upper()} ({creds['role']}) ---")
    token = login(creds["email"], creds["password"])
    if not token:
        continue
    
    for ep_name, ep_def in endpoints.items():
        status = test_endpoint(token, ep_def)
        print(f"{ep_name} -> {status}")
