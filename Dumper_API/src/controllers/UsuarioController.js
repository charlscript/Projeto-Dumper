const Usuario = require("../models/Usuario");

module.exports = {
    async index(request, response) {
        return response.json(await Usuario.find({}));
    },

    async delete(req, res) {
        const deleted = await Usuario.findByIdAndRemove(req.params.id);
        if (deleted) {
            return res.json({
                message: `Usuario de ID ${req.params.id} excluído`
            });
        }
    },

    async store(req, res) {
        const { nome } = req.body;
        const { email } = req.body;
        const { password } = req.body;

        const userExists = await Usuario.findOne({
            email: email
        });

        if (userExists) {
            return res.json({
                erro: "Usuário já existe"
            });
        }

        return res.json(
            await Usuario.create({
                nome: nome,
                email: email,
                password: password
            })
        );
    },

    async login(req, res) {
        const { email } = req.body;
        const { password } = req.body;

        await Usuario.findOne({ email: email }, function(err, usuario) {
            if (err) throw err;
            if (!usuario) {
                isMatch = 'Usuário não encontrado'
                return res.json({ isMatch })
            }
            usuario.comparePassword(password, function(err, isMatch) {
                if (err) throw err;
                const { nome } = usuario;
                return res.json({ isMatch, nome });
            });
        });
    },

};

//axios = requisição em outras API's
//00:45 vid 02