const express = require('express');
const mongoose = require('mongoose');
const axios = require('axios');

const app = express();
const PORT = 3000;

//  MongoDB
mongoose.connect('mongodb://localhost:27017/userdb', {
    useNewUrlParser: true,
    useUnifiedTopology: true,
});

// User Schema
const userSchema = new mongoose.Schema({
    id: Number,
    name: String,
    username: String,
    email: String,
});

const User = mongoose.model('User', userSchema);

//  Endpoint
app.get('/load', async (req, res) => {
    try {
        const response = await axios.get('https://jsonplaceholder.typicode.com/users');
        const users = response.data;

        await User.insertMany(users);
        res.status(200).send('Users loaded successfully');
    } catch (error) {
        res.status(500).send('Error loading users');
    }
});

//server
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});