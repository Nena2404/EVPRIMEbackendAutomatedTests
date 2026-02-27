const { Client } = require('pg');

const client = new Client({
  user: 'postgres',
  password: 'Soda.Voda123',
  host: 'localhost',
  port: '5432',
  database: 'evprime'
});

client.connect();

exports.dbClient = client;