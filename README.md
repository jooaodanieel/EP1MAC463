
# MAC 463 - EP1 

Versão do aplicativo implementado em Android (Java) 

### Grupo:
* Victor Wichmann Raposo - 9298020
* Joao Francisco Lino Daniel - 7578279

### Ambiente

Para utilizar o sistema de confirmação é necessário um aplicativo que scaneia codigos de barras e QR code. 

Utilizamos:

* PlayStore: "https://play.google.com/store/apps/details?id=com.google.zxing.client.android&hl=pt-BR"
* APK download: JOAAOO Achei esse, mas temos que testar no Genymotion (http://www.apkmirror.com/apk/zxing-team/barcode-scanner/barcode-scanner-4-7-3-2-release/barcode-scanner-4-7-3-2-android-apk-download/)

## Como executar

(Nao sei o que colocar)


### Como Usar

Implementamos dois metodos de confirmação, um aluno pode scanear um QR code de um seminário para se matricular. O outro, o professor pode scanear um codigo de barras com o número USP do aluno (presente na parte de tras do cartão de estudante) para confirmar a presença do aluno no semiário


- Área do Professor
    - [x] login
    - [x] alterar seu cadastro
    - [x] cadastro de novos professores
    - [x] cadastro de seminários
        - [x] visualizar os detalhes do seminário
        - [x] listar os alunos que assistiram cada seminário
    - [ ] Confirmar aluno (scaneando carterinha USP

- Área do Aluno
    - [x] login
    - [x] se cadastrar no sistema
    - [x] alterar seu cadastro
    - [x] consultar a lista de seminários

- [x] Salvar login (Shared Preferences)
- [ ] testes automatizados
- [ ] cache de informações criadas "offline"
- [ ] Material Design
- [ ] Boas Práticas (Organização do código e padronização)
- [x] Back na profile deve dar logout? App quebrando quando da back no login
- [x] Progress Dialog Log in/ SignUp
- [ ] Side menu
