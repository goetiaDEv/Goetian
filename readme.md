# 🔐 Ferramenta de Criptografia de Arquivos

Uma aplicação Java completa para criptografia e descriptografia de arquivos usando algoritmos padrão da indústria (AES-256 e RSA-2048).

![Java](https://img.shields.io/badge/Java-11+-orange?style=flat&logo=java)
![License](https://img.shields.io/badge/License-MIT-blue.svg)
![Status](https://img.shields.io/badge/Status-Ativo-success)

## 📋 Índice

- [Características](#-características)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação](#-instalação)
- [Como Usar](#-como-usar)
- [Algoritmos Suportados](#-algoritmos-suportados)
- [Segurança](#-segurança)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Contribuindo](#-contribuindo)
- [Licença](#-licença)
- [Contato](#-contato)

## ✨ Características

- **Interface Gráfica Intuitiva**: Interface Swing moderna e fácil de usar
- **Múltiplos Algoritmos**: Suporte para AES-256 e RSA-2048
- **Gerenciamento de Chaves**: Geração, salvamento e carregamento de chaves
- **Processamento Assíncrono**: Barra de progresso para arquivos grandes
- **Log Detalhado**: Histórico completo de operações
- **Segurança Robusta**: Implementação seguindo melhores práticas
- **Multiplataforma**: Funciona em Windows, Linux e macOS

## 🔧 Pré-requisitos

- **Java Development Kit (JDK) 11 ou superior**
- **Sistema operacional**: Windows, Linux ou macOS

## 📦 Instalação

### Opção 1: Executar diretamente

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/file-encryption-tool.git
cd file-encryption-tool
```

2. Compile o projeto:
```bash
javac -d out src/FileEncryptionTool.java
```

3. Execute a aplicação:
```bash
java -cp out FileEncryptionTool
```

### Opção 2: Usando o JAR executável

1. Baixe o arquivo `file-encryption-tool.jar` da seção [Releases](../../releases)

2. Execute o JAR:
```bash
java -jar file-encryption-tool.jar
```

### Opção 3: Compilar com Maven

```bash
mvn clean compile exec:java
```

## 🚀 Como Usar

### Criptografia com AES-256

1. **Selecione o Arquivo**: Clique em "Selecionar" e escolha o arquivo desejado
2. **Configure o Algoritmo**: Selecione "AES-256" no dropdown
3. **Digite a Senha**: Insira uma senha forte no campo "Senha (AES)"
4. **Gere a Chave**: Clique em "Gerar Chave" (opcional - será gerada automaticamente)
5. **Criptografe**: Clique no botão "Criptografar"

### Criptografia com RSA-2048

1. **Selecione o Arquivo**: Escolha o arquivo a ser criptografado
2. **Configure o Algoritmo**: Selecione "RSA-2048"
3. **Gere as Chaves**: Clique em "Gerar Chave" para criar o par de chaves
4. **Salve as Chaves**: Use "Salvar" para armazenar as chaves pública e privada
5. **Criptografe**: Use a chave pública para criptografar

### Descriptografia

1. **Selecione o Arquivo Criptografado**: Arquivo com extensão `.encrypted`
2. **Carregue a Chave**: Use "Carregar" para importar a chave necessária
3. **Descriptografe**: Clique em "Descriptografar"

## 🔒 Algoritmos Suportados

### AES-256 (Advanced Encryption Standard)
- **Tipo**: Criptografia simétrica
- **Tamanho da Chave**: 256 bits
- **Modo**: CBC (Cipher Block Chaining)
- **Padding**: PKCS5
- **IV**: Único para cada arquivo
- **Ideal para**: Arquivos grandes, uso pessoal

### RSA-2048 (Rivest-Shamir-Adleman)
- **Tipo**: Criptografia assimétrica
- **Tamanho da Chave**: 2048 bits
- **Modo**: ECB
- **Padding**: PKCS1
- **Tamanho máximo por bloco**: 245 bytes
- **Ideal para**: Compartilhamento seguro, pequenos arquivos

## 🛡️ Segurança

### Medidas Implementadas

- **Geração Segura de Chaves**: Uso de `SecureRandom` e algoritmos padrão
- **IV Único**: Cada arquivo AES recebe um IV único
- **Limpeza de Memória**: Senhas são removidas da memória após uso
- **Validação de Entrada**: Verificação de arquivos e chaves antes do processamento
- **Tratamento de Erros**: Gerenciamento seguro de exceções

### Recomendações de Uso

- ✅ Use senhas fortes para AES (min. 12 caracteres)
- ✅ Mantenha as chaves privadas RSA em local seguro
- ✅ Faça backup das chaves em local protegido
- ✅ Teste a descriptografia após criptografar
- ❌ Não compartilhe chaves privadas
- ❌ Não use senhas simples ou previsíveis

## 📁 Estrutura do Projeto

```
file-encryption-tool/
├── src/
│   └── FileEncryptionTool.java    # Código principal
├── docs/
│   ├── screenshots/               # Capturas de tela
│   └── manual.md                 # Manual detalhado
├── examples/
│   ├── sample-keys/              # Chaves de exemplo
│   └── test-files/               # Arquivos de teste
├── build/
│   └── file-encryption-tool.jar  # JAR executável
├── .gitignore
├── README.md
├── LICENSE
├── CONTRIBUTING.md
└── pom.xml                       # Maven configuration
```

## 🎯 Exemplos de Uso

### Exemplo 1: Criptografar documento pessoal
```
Arquivo: documento.pdf (2.5 MB)
Algoritmo: AES-256
Senha: MinhaSenh@Segura123!
Resultado: documento.pdf.encrypted
```

### Exemplo 2: Compartilhar arquivo com segurança
```
Arquivo: contrato.docx (150 KB)
Algoritmo: RSA-2048
Chave Pública: contrato_public.key
Resultado: contrato.docx.encrypted
```

## 🤝 Contribuindo

Contribuições são bem-vindas! Por favor, leia [CONTRIBUTING.md](CONTRIBUTING.md) para detalhes sobre nosso código de conduta e processo de submissão de pull requests.

### Como Contribuir

1. Faça um Fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 🐛 Reportar Bugs

Encontrou um bug? Por favor, abra uma [issue](../../issues) com:

- Descrição detalhada do problema
- Passos para reproduzir
- Sistema operacional e versão do Java
- Logs de erro (se houver)

## 📈 Roadmap

- [ ] Suporte para mais algoritmos (ChaCha20, Blowfish)
- [ ] Criptografia de diretórios completos
- [ ] Interface de linha de comando
- [ ] Integração com serviços de nuvem
- [ ] Assinatura digital de arquivos
- [ ] Modo de destruição segura

## 📄 Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 👨‍💻 Autor

**Seu Nome**
- GitHub: [@seu-usuario](https://github.com/goetiaDEv)
- LinkedIn: [Seu Perfil](https://www.linkedin.com/in/ezequiel-abreu-)
- Email: goetiadev@proton.me

## ⚠️ Aviso Legal

Esta ferramenta é fornecida "como está" para fins educacionais e de uso pessoal. Os desenvolvedores não se responsabilizam por perdas de dados ou uso inadequado da ferramenta. Sempre faça backup de arquivos importantes antes de criptografá-los.

---

⭐ **Se este projeto foi útil para você, considere dar uma estrela no repositório!**
