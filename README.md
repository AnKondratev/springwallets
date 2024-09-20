
                        
<div class="markdown-body"><h1 id="wallet-service">Wallet Service</h1>
<p>Это приложение предоставляет REST API для управления электронными кошельками. Оно позволяет выполнять операции по пополнению и снятию средств, а также получать баланс кошелька. Приложение разработано с использованием Java (версии 8-17), Spring (версии 3) и PostgreSQL. Для миграций базы данных используется Liquibase. </p>
<h2 id="функциональность">Функциональность</h2>
<h3 id="эндпоинты">Эндпоинты</h3>
<ol>
  <li><p><strong>Создание кошелька</strong></p>
<ul>
<li><strong>URL:</strong> <code>POST api/v1/save_wallet</code></li>
  <p>В теле запроса указывается только начальный баланс кошелька, UUID генерируется автоматически.<p></ul>
<li><strong>Тело запроса:</strong></li></ul>
<pre><code class="json language-json hljs"> <span class="hljs-punctuation">{</span>
   <span class="hljs-attr">"amount"</span><span class="hljs-punctuation">:</span> <span class="hljs-number">1000</span>
 <span class="hljs-punctuation">}</span>
 </code></pre>
<li><p><strong>Изменение кошелька</strong></p>
<li><strong>Тело запроса:</strong></li></ul>
<ul>
<li><strong>URL:</strong>В теле запроса  <code>POST api/v1/wallet</code></li>
<p>В теле запроса operationType указывается один из видов транзакции: DEPOSIT или WITHDRAW.<p>
<li><strong>Тело запроса:</strong></li></ul>
<pre><code class="json language-json hljs"> <span class="hljs-punctuation">{</span>
   <span class="hljs-attr">"walletId"</span><span class="hljs-punctuation">:</span> <span class="hljs-string">"UUID"</span><span class="hljs-punctuation">,</span>
   <span class="hljs-attr">"operationType"</span><span class="hljs-punctuation">:</span> <span class="hljs-string">"DEPOSIT"</span> | <span class="hljs-string">"WITHDRAW"</span><span class="hljs-punctuation">,</span>
   <span class="hljs-attr">"amount"</span><span class="hljs-punctuation">:</span> <span class="hljs-number">1000</span>
 <span class="hljs-punctuation">}</span>
 </code></pre>
<ul>
<li><strong>Описание:</strong> Операция пополнения или снятия средств с кошелька с указанным <code>walletId</code>.</li></ul></li>
<li><p><strong>Получение баланса кошелька</strong></p>
<ul>
<li><strong>URL:</strong> <code>GET api/v1/wallets/{WALLET_UUID}</code></li>
<li><strong>Описание:</strong> Получение текущего баланса для заданного кошелька.</li></ul></li>
</ol>
</ul>
<h2 id="конкурентная-среда">Конкурентная среда</h2>
<p>Приложение спроектировано с учётом работы в высоконагруженной среде. Специальные механизмы блокировок обеспечивают корректность работы при одновременных запросах к одному кошельку (до 1000 RPS).</p>
<h2 id="технологии-и-инструменты">Технологии и инструменты</h2>
<ul>
<li>Java (версия 8-17)</li>
<li>Spring (версия 3)</li>
<li>PostgreSQL</li>
<li>Liquibase</li>
<li>Docker &amp; Docker Compose</li>
</ul>
<h2 id="запуск-приложения">Запуск приложения</h2>
<h3 id="структура-проекта">Структура проекта</h3>
<p>Перед запуском убедитесь, что у вас установлен Docker и Docker Compose. </p>
<h3 id="конфигурация">Конфигурация</h3>
<p>Параметры как для приложения, так и для базы данных могут быть настроены через переменные окружения в файле <code>docker-compose.yml</code>.</p>
<h3 id="команды-для-запуска">Команды для запуска</h3>
<ol>
<li>Клонируйте репозиторий:</li>
</ol>
<pre><code class="bash language-bash hljs">   git <span class="hljs-built_in">clone</span> &lt;URL_вашего_репозитория&gt;
   <span class="hljs-built_in">cd</span> &lt;имя_папки_репозитория&gt;</code></pre>
<ol start="2">
<li>Запустите приложение и базу данных с помощью Docker Compose:</li>
</ol>
<pre><code class="bash language-bash hljs">   docker-compose up --build</code></pre>
<ol start="3">
<li>Ваше приложение будет доступно по адресу <code>http://localhost:8080</code>.</li>
</ol>
<h2 id="лицензия">Лицензия</h2>
<p>Этот проект расположен под лицензией MIT. Пожалуйста, ознакомьтесь с файлом LICENSE для получения дополнительной информации.</p>
<hr>
</div></div>
