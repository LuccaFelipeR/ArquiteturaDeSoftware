# Arquitetura Cliente-Servidor

No HospitalCare, a ideia é utilizar a arquitetura cliente-servidor para separar a parte que o usuário acessa da parte que processa as informações.

Por exemplo, quando a secretária cadastra um paciente, ela preenche os dados na tela e envia para o servidor. O servidor fica responsável por verificar as informações e realizar o cadastro.

O cliente é basicamente a interface do sistema, onde são feitas as solicitações e apresentados os resultados. Já o servidor cuida das validações, das regras de negócio e do acesso ao banco de dados.

Dessa forma, cada parte tem sua função e o sistema fica mais organizado.