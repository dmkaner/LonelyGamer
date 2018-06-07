const functions = require('firebase-functions');

const request = require('request-promise')

exports.indexGamesToElastic = functions.database.ref('/Games/{game_id}')
  .onWrite(event => {
    let gameData = event.data.val();
    let game_id = event.params.game_id;

    console.log('Indexing the post:', gameData);

    let elasticSearchConfig = functions.config().elasticsearch;
    let elasticSearchUrl = elasticSearchConfig.url + 'games/game/' + game_id;
    let elasticSearchMethod = gameData ? 'POST' : 'DELETE';
    let elasticSearchRequest = {
      method: elasticSearchMethod,
      url: elasticSearchUrl,
      auth:{
        username: elasticSearchConfig.username,
        password: elasticSearchConfig.password
      },
      body: gameData,
      json: true
    };
    return request(elasticSearchRequest);


  });
