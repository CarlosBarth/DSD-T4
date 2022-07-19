Mensageiro (messenger like)

RF_01 O sistema deve controlar o acesso de usuários (login)
RF_02 O sistema deve manter usuários (cadastro)
RF_03 O sistema deve permitir que o usuário inicie uma conversa individual
RF_04 O sistema deve permitir que o usuário inicie uma conversa em grupo
RF_05 O sistema deve permitir visualização atraves de log's (no Servidor) de todas as mensagens e ações realizadas no sistema
RF_06 O sistema deve permitir o envio de mensagens sem a utilização de click de mouse (enviar ao pressionar enter)

RNF1. O sistema deve ser distribuído e executar simultaneamente em vários clientes (hosts) diferentes.
RNF2. O sistema deve possuir um servidor de controle de troca de mensagens.
RNF3. O sistema deve se comunicar através de Sockets.
RNF4. O sistema não deve permitir Sockets ociosos e Não é deverá fazer o uso de serialização de objetos.
RNF5. A aplicação cliente deve ter interface gráfica.
RNF6. A interface gráfica deve ser desacoplada do restante do sistema, para que seja possível testar 
as comunicações sem a interface gráfica.


O sistema permite a troca de mensagens entre usuários atraves de mensagens privadas entre dois usuários
ou conversas em grupos de dois ou mais usuários.
As mensagens trocadas entre cliente e servidor são controladas através de um log geral no servidor onde cada método
disparado nos clientes gere um log específico na tela do servidor.
As menssagens trocadas são as seguintes:

Servidor -> Cliente
- Identificação de Usuário cadastrado
- Autenticação de usuário
- Quanidade de conversas do usuário logado (no ato do log in)

Cliente -> Servidor
- Solicitaçao de autenticação de usuário
- Login de usuário contendo as iformações do usuário logado
- Cadastro de usuário (dados do cadastro)
- Mensagens trocadas nas conversas










