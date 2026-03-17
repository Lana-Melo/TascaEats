# Check if Java is installed; if not, attempt to install OpenJDK 21.
if ! command -v java >/dev/null 2>&1; then
  echo "Java not found."
  if command -v apt-get >/dev/null 2>&1; then
    echo "Attempting to install OpenJDK 21 via apt-get..."
    sudo apt-get update && sudo apt-get install -y openjdk-21-jre-headless
  elif command -v brew >/dev/null 2>&1; then
    echo "Attempting to install OpenJDK 21 via Homebrew..."
    brew install openjdk@21
  else
    echo "No supported package manager found. Please install Java manually."
    exit 1
  fi
fi

pip install pre-commit
export PATH="$HOME/.local/bin:$PATH"
pre-commit install
