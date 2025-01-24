<h1>Descrição do Projeto</h1>
    <p>Este projeto consiste na implementação de uma API REST para gerenciar contas bancárias, utilizando apenas as bibliotecas nativas do Java (sem frameworks como Spring Boot). O sistema possui funcionalidades básicas para criar contas, consultar saldo, realizar depósitos e saques, além de deletar contas.</p>
    <p>A autenticação é feita por meio de tokens de sessão para garantir a segurança nas operações. A senha dos clientes é armazenada criptografada em MD5.</p>

<h2>Requisitos Funcionais</h2>
    <h3>Acessar Conta:</h3>
    <ul>
        <li>Login com CPF e senha.</li>
        <li>Gerar um token de sessão para autenticar as requisições subsequentes.</li>
    </ul>
    <h3>Operações Disponíveis:</h3>
    <ul>
        <li>Consultar saldo.</li>
        <li>Depositar dinheiro.</li>
        <li>Sacar dinheiro.</li>
        <li>Deletar conta.</li>
    </ul>
    <h2>Regras de Negócio</h2>
    <ul>
        <li>Não pode haver mais de um cliente com o mesmo CPF.</li>
        <li>A senha de um cliente pode ser igual à de outros.</li>
        <li>A senha será armazenada no banco de dados criptografada em MD5.</li>
        <li>Toda comunicação com o banco deve ser autenticada com um token de sessão.</li>
        <li>Caso o token não seja válido, solicitar login novamente e gerar um novo token.</li>
    </ul>
    <h2>Modelagem do Banco de Dados</h2>
    <h3>Entidades:</h3>
    <ul>
        <li>Cliente</li>
        <li>Conta</li>
    </ul>
    <h3>Relacionamento</h3>
    <p>Um cliente possui uma única conta (1:1).</p>
    <h3>Atributos:</h3>
    <h4>Cliente:</h4>
    <ul>
        <li><strong>idCliente:</strong> Identificador único do cliente (PRIMARY KEY).</li>
        <li><strong>nome:</strong> Nome do cliente (VARCHAR).</li>
        <li><strong>cpf:</strong> CPF do cliente (VARCHAR, NOT NULL, UNIQUE).</li>
        <li><strong>senha:</strong> Senha do cliente (VARCHAR, NOT NULL, armazenada como hash MD5).</li>
        <li><strong>token:</strong> Token de sessão do cliente (VARCHAR, gerado no login).</li>
        <li><strong>dataDeCriacaoDaConta:</strong> Data de criação da conta (DATE, NOT NULL).</li>
        <li><strong>idConta:</strong> Identificador da conta associada (FOREIGN KEY referenciando a tabela Conta).</li>
    </ul>
    <h4>Conta:</h4>
    <ul>
        <li><strong>idConta:</strong> Identificador único da conta (PRIMARY KEY).</li>
        <li><strong>idCliente:</strong> Identificador do cliente associado (FOREIGN KEY referenciando a tabela Cliente).</li>
        <li><strong>saldo:</strong> Saldo da conta (DOUBLE, com valor inicial fixo de 0, NOT NULL).</li>
    </ul>
    <h2>Endpoints da API</h2>
    <h3>Autenticação</h3>
    <ul>
        <li><strong>POST /login:</strong> Login do cliente com CPF e senha, retornando um token de sessão.</li>
        <li><strong>POST /logout:</strong> Logout do cliente, invalidando o token atual.</li>
    </ul>
    <h3>Gerenciamento de Cliente</h3>
    <ul>
        <li><strong>POST /register:</strong> Criação de uma nova conta e cliente.</li>
        <li><strong>DELETE /delete-account:</strong> Exclusão da conta e do cliente.</li>
    </ul>
    <h3>Operações Bancárias</h3>
    <ul>
        <li><strong>GET /balance:</strong> Consulta do saldo da conta do cliente autenticado.</li>
        <li><strong>POST /deposit:</strong> Realiza um depósito na conta do cliente.</li>
        <li><strong>POST /withdraw:</strong> Realiza um saque na conta do cliente.</li>
    </ul>
    <h2>Detalhes Técnicos</h2>
    <h3>Autenticação:</h3>
    <ul>
        <li>Cada requisição protegida exige um token de sessão válido.</li>
        <li>Caso o token seja inválido ou ausente, o cliente deve realizar o login novamente para obter um novo token.</li>
    </ul>
    <h3>Armazenamento Seguro:</h3>
    <ul>
        <li>As senhas são criptografadas utilizando o algoritmo MD5 antes de serem armazenadas no banco de dados.</li>
        <li><strong>Nota:</strong> MD5 é usado neste projeto para simplificação, mas não é recomendado para sistemas de produção. Algoritmos como bcrypt ou Argon2 são mais seguros.</li>
    </ul>
    <h3>Validação:</h3>
    <ul>
        <li>O CPF deve ser único e validado antes da criação do cliente.</li>
        <li>Depósitos e saques devem verificar se o valor é positivo.</li>
        <li>Saques devem garantir que o saldo da conta seja suficiente.</li>
    </ul>