# Cincecartaz

## Aplicação Móvel Android Nativa que permite ao utilizador registar os filmes que já viu, atribuindo-lhe algumas informações de cariz pessoal, tais como, a qualidade do filme quantitivamente, o cinema onde o mesmo foi visualizado, entre outras.

Este projeto foi desenvolvido para a cadeira de Computação Móvel (2022/2023).

A navegação da aplicação foi realizada através de uma BottomNavigationBar com acessos aos 4 ecrãs principais:
* Dashboard/Home
* Lista de Filmes
* Mais Páginas (Mapa & Extra)
* Registo de Filme

A BottomNavigationBar apresenta tambem um FloatingActionButton para se conseguir aceder a procura de filmes por voz.

Decidimos ainda criar animações de slide para a esquerda e direita quando se carrega na BottomNavigationBar.

<br>

## Dashboard / Home
É o primeiro ecrã a ser apresentado assim que se entra na aplicação. Neste ecrã o utilizador pode verirficar as seguintes informações:
* Último Filme Visto
* Estatísticas

<br>

Na parte "Último Filme Visto" o utilizador irá ter acesso as seguintes informações:
* Dados do último filme visto
* Data em que foi visto
* Cinema onde foi visto

É possível aceder aos detalhes do filme se clicarmos no poster ou nas informações do filme.

<br>

Na parte "Estatísticas" o utilizador irá ter acesso as seguintes informações:
* Género mais visto
* Total filmes vistos
* Ator mais visto
* Diretor mais visto
* Cinema mais visitado
* Total tempo visto

<br>

<p align="center">
  <img width="25%" height="25%" src="https://user-images.githubusercontent.com/79208470/233794252-2983aa0d-d53b-4753-b14a-feb0602b69de.png">
</p>

<br>

## Lista
Neste ecrã o utilizador tem acesso a uma lista de todos os filmes que já viu. A implementação desta lista foi através de uma RecyclerView. 

<br>

Nesta lista (vertical) os filmes apresentados mostram a seguinte informação:
* Poster
* Titulo & Ano
* Géneros
* Diretores
* Atores
* IMDb Rating
* IMDb Votos

Ao clicarmos num filme o utilizador vai ser levado para a página de detalhes do mesmo.

<br>

Foi tambem implementada a lista horizontal que apresenta a mesma informação que a lista vertical com dois extras:
* Rating do utilizador
* Google Maps

Ao clicarmos no Google Maps o utilizador vai ser levado para a página do Mapa, (enviando o id do cinema onde o filme foi visto), focando a camera no cinema onde viu esse filme.

<br>

<br>

<p align="center">
  <img width="25%" height="25%" src="https://user-images.githubusercontent.com/79208470/233796400-f321d349-c543-4d6c-b206-6cea08b08633.png">
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img width="50%" height="50%" src="https://user-images.githubusercontent.com/79208470/233796496-18b5bb6f-b246-496a-876d-90efd327cb37.png"> 
</p>


<br>

## Detalhes
Neste ecrã o utilizador tem acesso a dois tipos de informações:
* Dados do Filme
* Experiência do Utilizador

<br>

Na parte "Dados do Filme" o utilizador irá ter acesso as seguintes informações:
* Poster
* Titulo
* Ano de Lançamento
* Tempo do Filme
* IMDb Rating
* IMDb Votos
* Géneros
* Sinopse
* Diretores
* Atores
* Link para o IMDb

<br>

Na parte "Experiência do Utilizador" o utilizador irá ter acesso as seguintes informações:
* Cinema onde viu
* Data de visualização
* Rating
* Comentários (opcional)
* Fotografias (opcional)

<br>

<p align="center">
  <img width="25%" height="25%" src="https://user-images.githubusercontent.com/79208470/233796780-46b707f3-9f03-419b-90a5-2998ff2b135a.png">
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img width="25%" height="25%" src="https://user-images.githubusercontent.com/79208470/233796781-5fcea4ba-9a6f-4560-823d-b784ec8bc960.png"> 
</p>

<br>

Exemplo de um filme com fotografias.

NOTA: A implementação da lista de fotografias é uma RecyclerView horizontal

<br>

<p align="center">
  <img width="25%" height="25%" src="https://user-images.githubusercontent.com/79208470/233797031-a9b050c9-755e-4365-a392-6ab5c91586d7.png">
</p>

<br>

## Registo
Neste ecrã o utilizador pode registar um filme que tenha visto atribuindo-lhe algumas informações de cariz pessoal.

Para ser possível registar um filme, o utilizador terá que preencher um formulário composto pelos seguintes campos:
* Nome do Filme 
* Cinema
* Rating
* Data 
* Fotografias (opcional)
* Comentários (opcional)

Para garantirmos que escolhemos o filme correto, foi implementada uma AutoCompleteTextView que irá pesquisar, na API fornecida, à medida que se vai escrevendo e retorna os filmes com os posters, deste modo a facilitar o registo ao utilizador.

Para o cinema foi implementado um Dropdown Menu que apresenta todos os cinemas provenientes do JSON disponibilizado. O valor default, se a localização tiver sido ativada será o cinema que se encontra mais próximo do utilizador.

Ao clicarmos no botão "REGISTO" e todos os campos estarem preenchidos corretamente, o utilizador é levado automaticamente para a lista de filmes.

NOTA: Se o utilizador registar novamente um filme já visto, os dados anteriores irão ser atualizados com os novos.

NOTA: Não é possível registar filmes novos quando se está offline, apenas é possível reescrever os filmes já registados

<br>

<p align="center">
  <img width="25%" height="25%" src="https://user-images.githubusercontent.com/79208470/233797440-6185682c-b3a8-48f4-b0a3-f240bdbd1eaa.png">
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img width="25%" height="25%" src="https://user-images.githubusercontent.com/79208470/233797479-5965e125-599b-4759-81fb-ce762b128b60.png"> 
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img width="25%" height="25%" src="https://github.com/ULHT-CM-2022-23/projeto-android-nativo-22004883-22007130/assets/79208390/d0fef31b-4ab6-4e0d-ae2a-681cd9765a72.png">
</p>


<br>

## Mais Paginas
Neste ecrã é apresentado ao utilizador dois "cartões", sendo um deles para ser possível aceder ao Mapa e outro para o Extra.

<br>

<p align="center">
  <img width="25%" height="25%" src="https://user-images.githubusercontent.com/79208470/233797966-496eba39-eba3-4d11-9484-47c51538c77c.png">
</p>

<br>

## Extra
Ao entrar neste ecrã é feita uma animação chamada ButtonExplosion.

Neste ecrã é apresentada uma barra de pesquisa ao utilizador, que permite ao mesmo pesquisar por qualquer filme na API utilizando o AutoCompleteTextView para garantir ao utilizador o filme que escolhe é o correto.

Nota1: Esta pesquisa apenas funciona com conexão à internet.

Após ser feita a pesquisa do filme, o mesmo é apresentado com todos os seus dados (tal como na página de Detalhes) e uns extras:
* Prémios
* Trailer

NOTA2: O único ecrã que não apresenta o botão do microfone é o nosso ecrã horizontal do Extra que mostra o trailer, porque preferimos ser possível ver o trailer em um ecrã full screen e o botão a ocupar o ecrã iria estragar esse propósito. 

NOTA3: Se for a primeira vez que o utilizador esteja a pesquisar um filme e não pesquise nada e simplesmente rode o ecrão não será apresentado nenhum trailer de um filme, quando o utilizador finalmente pesquisar um filme ao rodar o ecra já irá aparecer o trailer do mesmo.

<br>

<p align="center">
  <img width="25%" height="25%" src="https://user-images.githubusercontent.com/79208470/233798295-93f54b2e-56f2-46e7-a806-5b49e1bf031d.png">
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img width="25%" height="25%" src="https://user-images.githubusercontent.com/79208470/233798322-b1e78bfb-c7ba-4c8d-8e5f-9e7f7f520225.png">  
</p>

<br>

<p align="center">
  <img width="50%" height="50%" src="https://user-images.githubusercontent.com/79208470/233798326-4518cbb6-aaf1-4e71-951b-a0d490e6da3b.png">
</p>

<br>

## Mapa
Neste ecrã o utilizador poderá aceder ao mapa e observar os locais(cinemas) onde viu os filmes que registou e ver quais as classificações que lhes deu.

<p align="center">
  <img width="25%" height="25%" src="https://github.com/ULHT-CM-2022-23/projeto-android-nativo-22004883-22007130/assets/79208390/6737baa4-e9f8-4230-9bac-392cfb4b138e.png">
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img width="25%" height="25%" src="https://github.com/ULHT-CM-2022-23/projeto-android-nativo-22004883-22007130/assets/79208390/bf88d262-f30d-44c2-8845-8b464323c832.png"> 
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img width="25%" height="25%" src="https://github.com/ULHT-CM-2022-23/projeto-android-nativo-22004883-22007130/assets/79208390/90084ed0-53b5-49ec-9fc0-840d5fa4321b">  
</p>

Ao clicar sobre um marcador se apenas tiver visto um filme nesse cinema, irá ser reencaminhado para a página de detalhes desse filme. Se por acaso o cinema tiver vários filmes registados,
ao clicar no marcador irá aparecer um pop up dialog onde será possível ver a lista de todos os filmes com a classificação dada pelo utilizador que depois se clicar num desses filmes da lista irá também para os detalhes desse filme

## Pesquisa Filme Por Voz


Ao pressionar o botão do microfone, um dialog irá aparecer e é possível dizer um nome do filme para se pesquisar, filme este que tem de estar já registado pelo utilizador. Se o utilizador entender também pode tocar ao lado/fora do dialog e a caixa desaparece.
Este dialog tem dois botões, um para dar reset ao que o microfone captou, por exemplo se o microfone não captar bem o que dissemos ou afinal queremos dizer outro filme, podemos dar reset e falar outra vez. E o botão de pesquisa que ao pressionarmos irá procurar o filme que dissemos, que se encontrar, então abre a página de detalhes se não encontrar irá ser mostrada uma mensagem de aviso.

<p align="center">
  <img width="25%" height="25%" src="https://github.com/ULHT-CM-2022-23/projeto-android-nativo-22004883-22007130/assets/79208390/52972d3d-1e85-45b5-8a8b-2e6fc579bf6b.png">
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img width="25%" height="25%" src="https://github.com/ULHT-CM-2022-23/projeto-android-nativo-22004883-22007130/assets/79208390/747ca19a-7471-4114-a7d8-576d99694aef.png">
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img width="25%" height="25%" src="https://github.com/ULHT-CM-2022-23/projeto-android-nativo-22004883-22007130/assets/79208390/9faa2446-81a9-4c21-a676-c2bcfbb5ebe4.png">
</p>


## Funcionamento Offline
A app mesmo sem conectividade mantém quase todas as funcionalidades disponíveis, apenas não sendo possível registar novos filmes, pois utiliza a API e o extra também fica incapacitado, pois utiliza a API dos filmes e do youtube.
De resto todos os outros ecrâs encontram-se disponíveis.
Quando não há conexão a app mostra no topo do ecrã um icon para o utilizador perceber que está offline.


<p align="center">
  <img width="25%" height="25%" src="https://github.com/ULHT-CM-2022-23/projeto-android-nativo-22004883-22007130/assets/79208390/367817e6-5028-4145-8626-6064f7331494.png">
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img width="25%" height="25%" src="https://github.com/ULHT-CM-2022-23/projeto-android-nativo-22004883-22007130/assets/79208390/70062e0b-07bd-413f-a405-fc8b89670a9b.png">
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img width="25%" height="25%" src="https://github.com/ULHT-CM-2022-23/projeto-android-nativo-22004883-22007130/assets/79208390/f2bdc75d-ae15-4e29-b05e-9621b9fe3f1a.png">
</p>


## Descricão das classes de lógica de negócio:
Classe MovieRoom:
  - Atributos:
    * title - String
    * releasedDate - String
    * duration - String
    * genre - String
    * poster - String
    * plot - String
    * imdbRating - String
    * imdbVotes - String
    * imdbID - String
    * actors - String
    * directors - String
    * awards - String
    * downloadedPoster - String
    * cinemaID - String
    * userRating - String
    * userSeenDate - String
    * userPhoto - List<String>
    * userObservations - String
    
  - Métodos:
    * getAllMoviesRegisteredByUser(movies: List<MovieRoom>): MutableList<Movie>
    * getMoviesSortedByDate(movies: List<MovieRoom): MutableList<Movie>
    * getMostSeenActorDirectorGenre(field: String,movies: List<MovieRoom): String
    * limitTextSize(text:String?,size:Int) :String?
    * getLastSeenMovie(movies: List<MovieRoom): Movie?
    * getTotalDurationsOfMoviesSeenByUser(movies: List<MovieRoom) : String
    * getTotalMoviesSeenByUser(movies: List<MovieRoom) : String
    * getTitle(): String
    * getRealesedDate(): String
    * getDuration(): String
    * getReleasedYear(): String
    * getGenre(): String
    * getPoster(): String
    * getPlot(): String
    * getImdbRating(): String
    * getImdbVotes(): String
    * getImdbID(): String
    * getActors(): String
    * getDirectors(): String
    * getAwards(): String
    * getCinemaID(): String
    * getUserRating(): String
    * getUserSeenDate(): String
    * getUserPhoto(): List<String>?
    * getUserObservations(): String
    * getTitleYear(): String
    * getOnlyYearReleaseDate(): String
    * getCinema(): Cinema?
    * getCinemaName(): String
    * setCinemaID(cinemaID: String)
    * setUserRating(rating: String)
    * setUserSeenDate(seenDate: String)
    * setUserPhoto(photos: List<String>)
    * setUserObservations(observations: String)
    * getRatingMarker(context: Context): String
    
    
Classe Cinema:
  - Atributos:
    * id - String
    * name - String
    * provider - String
    * latitude - String
    * longitude - String
    * address - String
    * postcode - String
    * county - String
    
  - Métodos:
    * getIDByName(name: String): String
    * getID(): String
    * getName(): String
    * getLatitude(): String
    * getLongitude(): String
    * readJsonFile(assets: AssetManager)
  
## Idiomas
  O idioma default é inglês, e os outros dois são portugês e espanhol, que irão ser utilizados caso o utilizador tenha o seu dispositivo em alguma destas duas linguas, se não irá ser utilizado o inglês.
  Utilizamos o google tradutor para realizar as traduções.
  
## Fontes de Informação utilizadas:
  
- Prompts chatgpt:  
  * how do i do an alertdialog in target 31 sdk
  * how to highlight some icon in the bottom navigation bar
  * make me a function that searches in youtube for a title and returns the first videos id, using this http request https://youtube.googleapis.com/youtube/v3/search?part=id&maxResults=1&q=teste&key=myapikey
  * How do I Use AutoCompleteTextView and populate it with data from a web API?
  * i have a google maps marker click listener and i want to get its coordinates when i click it or get its title
  * how to do a network listener

- Pesquisas youtube:  
  * https://www.youtube.com/watch?v=vc70qmG8d4Y -> animacao explosao do botao
  * https://www.youtube.com/watch?v=x6-_va1R788 -> bottom navigation bar com o fab no meio  
  * https://www.youtube.com/watch?v=0s6x3Sn4eYo& -> animacao slide para os ecras da bottom navigation bar  
  * https://www.youtube.com/watch?v=7FUICgnrprw& -> mostrar fotos com a biblioteca glide  
  * https://www.youtube.com/watch?v=7FUICgnrprw& -> utilizar uma intent para guardar varias fotos da galeria
  * https://www.youtube.com/watch?v=jb5KVZQBDio -> implementação do microfone
  * https://www.youtube.com/watch?v=B9jrhLyRwBs -> leitura do ficheiro json
  * https://www.youtube.com/watch?v=a_zcPmwRc_A -> salvar listas de string na room DB

- Pesquisas google: 
  * make a spinner in android -> https://developer.android.com/develop/ui/views/components/spinner
  * autocomplete textview in android -> https://developer.android.com/reference/android/widget/AutoCompleteTextView  
  * how to stop loosing data on rotate in android -> https://stackoverflow.com/questions/5123407/losing-data-when-rotate-screen
  

- Pesquisas para implementar o youtube na app:
  * https://www.youtube.com/watch?v=7FUICgnrprw& -> como usar o youtube player  
  * documentação/git para o youtube player -> https://github.com/PierfrancescoSoffritti/android-youtube-player  
  * documentação para enviar os pedidos http do youtube e criar a key para esses pedidos -> https://developers.google.com/youtube/v3/getting-started?hl=pt-br
  
  
## Link do video do youtube
  - https://youtu.be/EHKzdiohXys
  
## Nota Prevista:
  Resumindo o nosso projeto tem todas as funcionalidades pedidas no enunciado implementadas.
  ### 19
  
## Alunos:

Afonso Carreira 22004883<br>
Bernardo Vinagre 22007130
