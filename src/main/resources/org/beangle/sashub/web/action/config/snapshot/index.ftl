[@b.head "上传war"/]
<div class="container-fluid">
  [@b.messages slash="3"/]
  [@b.form action="!upload"   enctype="multipart/form-data" class="form-inline" role="form"]
    <label for="zipfile" class="control-label">选择带有时间戳的war文件：</label>
    <div class="form-group">
      <input type="file" name="warfile"  id="warfile" class="form-control">
    </div>
    <div class="form-group">
        [@b.submit class="btn btn-primary" value="上传"/]
    </div>
  [/@]
</div>
[@b.foot/]
