angular.module(
  'my-app',
  ['jsonforms', 'jsonforms-material']
).controller('DemoController', function($http) {

     var vm = this;
     vm.resolved = false;
     vm.uischema = {
       "type": "HorizontalLayout",
       "elements": [
         {
           "type": "Control",
           "scope": {
             "$ref": "#/properties/name"
           }
         },
         {
           "type": "Control",
           "scope": {
             "$ref": "#/properties/age"
           }
         }
       ]
     };

     $http.get('assets/schemas/person-schema.json')
       .then(function(res){
         vm.schema = res.data;
         vm.data = {
           "name": 'John Doe',
           "height": 1.85,
           "gender": 'Male'
         };
         vm.resolved = true;
       });

     vm.submit = function(personJson) {
        $http.post('/person', personJson)
          .then(function(res) {
            vm.isError = false;
            vm.msg = "Person has been added.";
          }, function(err) {
            var errorMessage = err.data.reduce(function(msg, error) {
               return msg + error.msgs.join(". ");
            }, "");
            vm.isError = true;
            vm.msg = errorMessage;
          });
     }
});
