const express = require("express");
const mongoose = require("mongoose");
const routes = require("./routes");

const server = express();
mongoose.connect(
    "mongodb+srv://LOGIN_ATLAS:SENHA_ATLAS@CLUSTER_aTLAS?retryWrites=true&w=majority", { useNewUrlParser: true });

server.use(express.json());
server.use(routes);

const port = process.env.PORT || 3333;
server.listen(port, () => {
    console.log("API rodando...");
});