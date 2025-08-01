[@b.head loadui=false]
  <title>Sas Admin Console</title>
[/@]
<style>
    #termLog {
        height: 750px;
        padding-bottom: 20px;
        padding-left: 10px;
        overflow-y: scroll;
        margin: 0;
        font: 14px Andale Mono, monospace;
        white-space: pre;
    }
    p{
      white-space: preserve-breaks;
    }
    #termLog pre{
      margin:0px;
      padding:0px;
    }
    #term {
      padding-left: 10px;
    }
</style>

<h4>Sas Admin Console</h4>
<div id="termLog"></div>

<div id="login">
    <!-- {which: 13} is a hack to make the button function the same as onkeypress -->
    <input id="name" type="text" placeholder="Your Name" onkeydown="startConnection(event);" value="/login admin changeit" style="display:none"/>
    <button id="connect" onclick="startConnection({which: 13});">Connect</button>
</div>
<div id="term" style="display:none;">
    <input id="text" type="text" value="/connect " placeholder="press enter to submit" onkeydown="chat(event);" style="width:500px;" /> <button id="send" onclick="chat({which: 13});">Send</button> <button id="disconnect" onclick="endConnection();">Disconnect</button>
</div>
<script src="${b.base}/static/scripts/websocket.js?v=2"></script>
<script>
  jQuery(document).ready(function(){
    jQuery("#connect").click();
  });
</script>
[@b.foot/]
