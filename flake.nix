{
  description = "IntelliJ SDK Java Home plugin development environment";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
      in
      {
        devShells.default = pkgs.mkShell {
          buildInputs = with pkgs; [
            # JetBrains Runtime with JCEF for IntelliJ plugin development
            jetbrains.jdk

            # Node.js for tooling
            nodejs_22

            # GitHub CLI
            gh

            # Gradle (optional, project has wrapper)
            gradle
          ];

          shellHook = ''
            export JAVA_HOME="${pkgs.jetbrains.jdk}"
            echo "IntelliJ SDK Java Home - Development Shell"
            echo "Java: $(java -version 2>&1 | head -1)"
            echo "Node: $(node --version)"
          '';
        };
      });
}
