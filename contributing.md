# 🤝 Contribuindo para File Encryption Tool

Obrigado por considerar contribuir para o File Encryption Tool! Este documento fornece diretrizes para contribuições.

## 📋 Índice

- [Código de Conduta](#código-de-conduta)
- [Como Contribuir](#como-contribuir)
- [Reportando Bugs](#reportando-bugs)
- [Sugerindo Melhorias](#sugerindo-melhorias)
- [Desenvolvimento](#desenvolvimento)
- [Padrões de Código](#padrões-de-código)
- [Processo de Pull Request](#processo-de-pull-request)
- [Configuração do Ambiente](#configuração-do-ambiente)

## 📖 Código de Conduta

Este projeto adere ao Contributor Covenant [código de conduta](CODE_OF_CONDUCT.md). Ao participar, você deve seguir este código. Por favor, reporte comportamentos inaceitáveis para [email@example.com].

## 🚀 Como Contribuir

### Tipos de Contribuições

Aceitamos vários tipos de contribuições:

- 🐛 **Correção de bugs**
- ✨ **Novas funcionalidades**
- 📝 **Melhorias na documentação**
- 🎨 **Melhorias na interface**
- 🔧 **Refatoração de código**
- 🧪 **Adição de testes**
- 🌐 **Traduções**

### Primeiros Passos

1. **Fork o repositório**
2. **Clone seu fork**
   ```bash
   git clone https://github.com/seu-usuario/file-encryption-tool.git
   cd file-encryption-tool
   ```
3. **Crie uma branch para sua contribuição**
   ```bash
   git checkout -b feature/minha-contribuicao
   ```

## 🐛 Reportando Bugs

### Antes de Reportar

- Verifique se o bug já foi reportado nas [issues existentes](../../issues)
- Teste com a versão mais recente
- Colete informações detalhadas sobre o problema

### Template de Bug Report

```markdown
**Descrição do Bug**
Uma descrição clara e concisa do que o bug é.

**Para Reproduzir**
Passos para reproduzir o comportamento:
1. Vá para '...'
2. Clique em '....'
3. Role para baixo até '....'
4. Veja o erro

**Comportamento Esperado**
Uma descrição clara e concisa do que você esperava que acontecesse.

**Screenshots**
Se aplicável, adicione screenshots para ajudar a explicar seu problema.

**Ambiente:**
 - OS: [e.g. Windows 10, Ubuntu 20.04]
 - Java Version: [e.g. OpenJDK 11, Oracle JDK 17]
 - Versão da Aplicação: [e.g. v1.0.0]

**Contexto Adicional**
Adicione qualquer outro contexto sobre o problema aqui.
```

## 💡 Sugerindo Melhorias

### Template de Feature Request

```markdown
**A sua feature request está relacionada a um problema? Descreva.**
Uma descrição clara e concisa de qual é o problema. Ex. Eu estou sempre frustrado quando [...]

**Descreva a solução que você gostaria**
Uma descrição clara e concisa do que você quer que aconteça.

**Descreva alternativas que você considerou**
Uma descrição clara e concisa de quaisquer soluções ou features alternativas que você considerou.

**Contexto adicional**
Adicione qualquer outro contexto ou screenshots sobre a feature request aqui.
```

## 🛠️ Desenvolvimento

### Configuração do Ambiente

1. **Pré-requisitos**
   - Java JDK 11 ou superior
   - Git
   - IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code)

2. **Clone e Setup**
   ```bash
   git clone https://github.com/seu-usuario/file-encryption-tool.git
   cd file-encryption-tool
   ```

3. **Compile e Execute**
   ```bash
   javac -d out src/FileEncryptionTool.java
   java -cp out FileEncryptionTool
   ```

### Estrutura do Projeto

```
src/
├── FileEncryptionTool.java      # Classe principal
├── crypto/                      # Módulos de criptografia (futura expansão)
├── ui/                         # Componentes de UI (futura expansão)
└── utils/                      # Utilitários (futura expansão)
```

## 📏 Padrões de Código

### Convenções Java

- **Nomenclatura**: Use camelCase para métodos e variáveis, PascalCase para classes
- **Indentação**: 4 espaços
- **Comprimento de linha**: Máximo 120 caracteres
- **Comentários**: Use Javadoc para métodos públicos

### Exemplo:

```java
/**
 * Criptografa um arquivo usando o algoritmo AES.
 * 
 * @param inputFile arquivo a ser criptografado
 * @param outputFile arquivo de saída criptografado
 * @throws Exception se ocorrer erro durante a criptografia
 */
private void encryptAES(File inputFile, File outputFile) throws Exception {
    // Implementação aqui
}
```

### Boas Práticas de Segurança

- **Nunca** faça commit de chaves reais ou senhas
- Use `Arrays.fill()` para limpar arrays de senha
- Valide todas as entradas do usuário
- Trate exceções de segurança adequadamente

## 🔄 Processo de Pull Request

### Antes de Submeter

1. **Teste sua mudança** completamente
2. **Atualize a documentação** se necessário
3. **Verifique o estilo de código**
4. **Execute todos os testes**

### Checklist do PR

- [ ] Meu código segue as diretrizes de estilo do projeto
- [ ] Realizei uma auto-revisão do meu código
- [ ] Comentei meu código, especialmente em áreas difíceis de entender
- [ ] Fiz mudanças correspondentes na documentação
- [ ] Minhas mudanças não geram novos warnings
- [ ] Testei que minha correção é eficaz ou que minha feature funciona
- [ ] Testes unitários novos e existentes passam localmente

### Template de Pull Request

```markdown
## Descrição

Breve descrição das mudanças realizadas.

## Tipo de Mudança

- [ ] Bug fix (mudança que não quebra funcionalidade existente e corrige um problema)
- [ ] Nova feature (mudança que não quebra funcionalidade existente e adiciona funcionalidade)
- [ ] Breaking change (correção ou feature que faria funcionalidade existente não funcionar como esperado)
- [ ] Esta mudança requer atualização da documentação

## Como Foi Testado?

Descreva os testes que você executou para verificar suas mudanças.

## Screenshots (se aplicável):

## Checklist:

- [ ] Meu código segue as diretrizes de estilo deste projeto
- [ ] Realizei uma auto-revisão do meu próprio código
- [ ] Comentei meu código, especialmente em áreas difíceis de entender
- [ ] Fiz mudanças correspondentes na documentação
- [ ] Minhas mudanças não geram novos warnings
- [ ] Adicionei testes que provam que minha correção é eficaz ou que minha feature funciona
- [ ] Testes unitários novos e existentes passam localmente com minhas mudanças
```

## 🧪 Testes

### Executando Testes

```bash
# Compilar
javac -d out src/FileEncryptionTool.java

# Executar testes manuais
java -cp out FileEncryptionTool
```

### Cenários de Teste

1. **Criptografia AES**:
   - Arquivo pequeno (< 1KB)
   - Arquivo médio (1-10MB)
   - Arquivo grande (> 10MB)
   - Senha vazia
   - Senha muito longa

2. **Criptografia RSA**:
   - Arquivo pequeno
   - Chave inválida
   - Chave não encontrada

3. **Interface**:
   - Seleção de arquivo inválido
   - Operações simultâneas
   - Cancelamento de operação

## 📚 Recursos Úteis

- [Oracle Java Cryptography Architecture](https://docs.oracle.com/en/java/javase/11/security/java-cryptography-architecture-jca-reference-guide.html)
- [Java Swing Tutorial](https://docs.oracle.com/javase/tutorial/uiswing/)
- [Git Flow Workflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow)

## 🎯 Roadmap de Desenvolvimento

### Próximas Features

1. **Suporte a mais algoritmos**
   - ChaCha20-Poly1305
   - Blowfish
   - 3DES

2. **Melhorias na UI**
   - Tema escuro
   - Internacionalização
   - Drag & drop de arquivos

3. **Funcionalidades avançadas**
   - Criptografia de diretórios
   - Linha de comando
   - Integração com cloud

## 🤝 Comunidade

- **Discussões**: Use [GitHub Discussions](../../discussions) para perguntas gerais
- **Issues**: Use [GitHub Issues](../../issues) para bugs e feature requests
- **Email**: Para questões privadas, contate [email@example.com]

## 📜 Versionamento

Seguimos [Semantic Versioning](https://semver.org/):

- **MAJOR**: Mudanças incompatíveis na API
- **MINOR**: Funcionalidade adicionada de forma compatível
- **PATCH**: Correções de bugs compatíveis

## 🏆 Reconhecimento

Todos os contribuidores serão reconhecidos no arquivo [CONTRIBUTORS.md](CONTRIBUTORS.md).

---

**Obrigado por contribuir! 🙏**

Sua contribuição torna este projeto melhor para todos. Se você tem dúvidas, não hesite em perguntar!