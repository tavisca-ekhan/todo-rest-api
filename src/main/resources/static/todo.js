'use strict'

function httpGetAsync(theUrl, callback)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
            callback(xmlHttp.responseText);
    }
    xmlHttp.open("GET", theUrl, false); // true for asynchronous
    xmlHttp.send(null);
}

function httpPatchAsync(theUrl, callback) {
    var xmlHttp = new XMLHttpRequest();
        xmlHttp.onreadystatechange = function() {
            if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
                callback(xmlHttp.responseText);
        }
        xmlHttp.open("PATCH", theUrl, false); // true for asynchronous
        xmlHttp.setRequestHeader("content-type","application/json");
        xmlHttp.send(null);
}

function httpPostAsync(theUrl,dataSend, callback)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
            callback(xmlHttp.responseText);
    }
    xmlHttp.open("POST", theUrl, false); // true for asynchronous
    xmlHttp.setRequestHeader("content-type","application/json");
    xmlHttp.send(dataSend);
}

function httpDeleteAsync(theUrl, callback)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
            callback(xmlHttp.responseText);
    }
    xmlHttp.open("DELETE", theUrl, false); // true for asynchronous
    xmlHttp.send(null);
}


// Basic Page Switching Code
function loadHome(){
    document.getElementById("entries-container").innerHTML=`
<div id="search-section">
    <!-- contains search box and add button -->
    Search And Add
    <input class="pad" type="text" id="search-box">

    <button class="pad" id="add-button" disabled="disabled" onclick="addItem()">ADD</button>
    <div id="search-results"></div>
</div>
<div id="entries">
    <!-- Entries and Actions Table Here -->
    <table>
        <thead>
            <tr>
                <th>Item</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody id="entry-table">
            <tr>
                <td>Pseudo Row</td>
                <td><button>Edit</button> <button>Remove</button></td>
            </tr>
        </tbody>
    </table>
</div>`;
// document.getElementById("search-box").addEventListener("keydown",eve=>{searchBoxQuery()});
document.getElementById("search-box").addEventListener("keyup",eve=>{
    searchBoxQuery();
    if (eve.keyCode === 13) {
        eve.preventDefault();
        document.getElementById("add-button").click();
      }
});
populateTable();
}

function loadUses(){
    document.getElementById("entries-container").innerHTML=`To-Do List Can Become a major part of your life.
                                                        All You have to do is to complete all the tasks present in this list.`
}

function loadAbout(){
    document.getElementById("entries-container").innerHTML=`This application is create by Aniket Singla with the use of HTML, CSS and Vanilla
    JavaScript. This is just a User Interface without any backend and hence all your saved data may disappear on shutdown of this application or Browser`
}

// Main Code

let data;

function populateTable(){
    let htmlCode = '';
    httpGetAsync('/todo',(json)=>{
                data = JSON.parse(json);
            })
    data.forEach(element => {
        htmlCode += `<tr>
                        <td>${element.item}</td>
                        <td><button onclick="editItemWizard(${element.id})">Edit</button> <button onclick="removeItem(${element.id})">Remove</button></td>
                    </tr>`
    });
    document.getElementById("entry-table").innerHTML = htmlCode;
}

function editItemWizard(id){
    document.getElementById("entries-container").innerHTML =   `<h5>Edit Item Wizard:</h5>
                                                                <input type="text" id="edit-item">
                                                                <button onclick="editItem(${id})">Save</button>`

}

function editItem(id){
    let newValue = document.getElementById("edit-item").value;
    httpPatchAsync(`/todo/${id}?item=${newValue}`, console.log);
    loadHome();
}

function removeItem(id){
//    data.splice(id,1);
//    localStorage.data = JSON.stringify(data);
    httpDeleteAsync(`/todo/${id}`,console.log);
    loadHome();
}

/*
    *Search And Add Functionality
*/

function searchBoxQuery()
{
    let toFind = document.getElementById('search-box').value.trim();
    let list=[];
    data.forEach(element=>{
        if(element.item.toLowerCase().startsWith(toFind.toLowerCase())){
            list.push(element);
        }

    })
    let button = document.getElementById("add-button");
    if((list.length>0 && list[0].item.toLowerCase()==toFind.toLowerCase())||toFind.trim()=="")
    {
        button.disabled=true;
        button.style.backgroundColor = "red";
    }

    else
    {
        button.disabled = false;
        button.style.backgroundColor = "green";

    }
    let htmlCode="";
    list.forEach(element => {
        htmlCode += `<tr>
                        <td>${element.item}</td>
                        <td><button onclick="editItemWizard(${element.id})">Edit</button> <button onclick="removeItem(${element.id})">Remove</button></td>
                    </tr>`
    });
    document.getElementById("entry-table").innerHTML = htmlCode;
}

function addItem(){
    let item = document.getElementById('search-box').value
    // data[data.length] = {item: item};
    // localStorage.data = JSON.stringify(data);
    // loadHome();
    if (item == "") {
        alert("Please enter any Item");
    } else {
        httpPostAsync('/todo',`{"item":"${item}"}`,console.log)
        loadHome();
    }
}

window.addEventListener("load", function() {
    document.getElementById("search-box").addEventListener("keyup",eve=>{
        searchBoxQuery();
        if (eve.keyCode === 13) {
            eve.preventDefault();
            document.getElementById("add-button").click();
          }
    });
});