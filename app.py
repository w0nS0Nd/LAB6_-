from flask import Flask, request, jsonify

app = Flask(__name__)

# In-memory 'database'
users = {}

@app.route("/register", methods=["POST"])
def register():
    data = request.get_json(silent=True) or {}
    if "email" not in data or "password" not in data:
        return jsonify({"status": "error", "message": "Invalid data"}), 400
    users[data["email"]] = data["password"]
    return jsonify({"status": "success"}), 201

@app.get("/health")
def health():
    return {"status": "ok"}, 200

if __name__ == "__main__":
    # Run development server
    app.run(host="127.0.0.1", port=5000, debug=True)
