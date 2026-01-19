import requests
import json
import time

BASE_URL = "http://localhost:8080/api/products"

def test_legacy_create():
    print("Testing Legacy Creation (categoryId)...")
    payload = {
        "name": f"Legacy Product Test {time.time()}",
        "price": 100.0,
        "description": "Testing legacy compatibility",
        "userId": 1,
        "categoryId": 1
    }
    
    try:
        response = requests.post(BASE_URL, json=payload)
        print(f"Status: {response.status_code}")
        
        if response.status_code in [200, 201]:
            data = response.json()
            print(f"Response: {data}")
            
            if 'category' in data and data['category'] is not None:
                print("SUCCESS: Legacy creation works AND 'category' field is present!")
                return True
            else:
                print("PARTIAL SUCCESS: Creation worked but 'category' field is MISSING.")
                return False
        else:
            print(f"FAILURE: Legacy creation failed. {response.text}")
            return False
    except Exception as e:
        print(f"EXCEPTION: {e}")
        return False

if __name__ == "__main__":
    test_legacy_create()
