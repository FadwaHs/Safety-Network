const express = require('express');
const http = require('http');
const axios = require('axios');
const socketIo = require('socket.io');
const app = express();
const server = http.createServer(app);
const io = socketIo(server);
const port = 3002;
const CrudServiceURL = 'http://localhost:3000'

io.on('connection',(socket)=>{
    console.log('We have a client');
    // Posting a location 
    socket.on('postcitizenlocation', (data) => {
        axios.post(`${CrudServiceURL}/postcitizenlocation`, data).then(
            (response)=>{
                socket.emit('postcitizenlocation',response.data);
                console.log("Client "+data.user_id+" has been served");
            }
        ).catch((error)=>{
            socket.emit('postcitizenlocation',error.response.data);
            console.log("Client "+data.user_id+" has been served in error");
            console.log(error.response.data);
        })          
    });
    // Getting a location
    socket.on('getlatestcitizenlocation', (data) => {
        axios.get(`${CrudServiceURL}/getlatestcitizenlocation/${data.user_id}`).then(
            (response)=>{
                socket.emit('getlatestcitizenlocation', response.data);
                console.log(response.data);
                console.log("Client "+data.user_id+" has been served");
            }
        ).catch((error)=>{    
            socket.emit('getlatestcitizenlocation',error.response.data);
            console.log("Client "+data.user_id+" has been served in error");
            console.log(error.response.data);
        })          
    });
    // I will not update or delete the location using a socket
})

server.listen(port, () => {
  console.log(`Serveur Express en cours d'exécution sur le port ${port}`);
});
