# sd-ufpb
## Enunciado
Modifique o código do cliente e servidor TCP da Prática 1 para desenvolver um bate-papo em linha de comando, onde tanto o servidor quanto o cliente podem digitar mensagens de uma para o outro. Vocês devem continuar usar Sockets para a comunicação baseada em troca de mensagens. Envie aqui na atividade o código desenvolvido, o link para o repositório do github que contém a sua solução (o professor deve ter acesso ao repositório
-usuário marcuswac) e explique o que foi feito.
Para pontuação completa, o sistema deve suportar um chat com múltiplos usuários (mais de 2).

## Explicação
O codigo para iniciar a aplicação esta na classe Main
### frontendS
Foi utilizada o pacote nativo do java, swing. Apenas duas telas foram criadas: 
- NetWorkSetup: nela o usuario decide se ira iniciar um servidor ou se conectar a um como cliente. Existe um filtro nos campos ip e port para imperdir que o usuario insira valores incoretos. O campo Ip fica bloqueado caso o usuario tente iniciar um servidor
- ChatInterface: uma tela de chat simples, o nome do usuario aparece no campo de titulo da tela.

### backend
De forma resumida, a aplicação utiliza um sitema de grupo, o grupo é mantido pelo servidor, quando um cliente envia uma mensagem para o grupo, é o servidor que recebe e fica responsavel por enviar a mensagem para os outros clientes do grupo, e quando o servidor enviar a mensagem ele envia para todos os clientes.
#### anotações
- Uma interface "SocketComunication" foi criada para padronizar o envio de mensagens o da classe ChatInterface com as classes que representão o cliente e o servidor. 
- Quando um cliente é gerado uma thread fica reponsavel por esperar mensagens do servidor sem bloquear a aplicação.
- Quando o servido é iniciado uma thread fica responsavel por esperar a conexão de novos clientes, que quando acontece gera uma nova thread para esperar mensagens do cliente. 
