<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 06.09.16
  Time: 16:46
  To change this template use File | Settings | File Templates.
--%>

<div class="control-group">
    <label class="control-label" for="tariff">Tariff</label>
    <div class="controls">
        <select id="tariff" name="tariff" class="form-control">
        </select>
    </div>
</div>

<div class="control-group">
    <label class="control-label" for="options">Options</label>
    <div class="controls">
        <div id="options" class="boxes"></div>
    </div>
</div>

<div class="control-group">
    <label class="control-label" for="number">Number</label>
    <div class="controls input-group">
        <span class="input-group-addon"><i class="fa fa-phone"></i> </span>
        <input type="text" id="number" name="number" placeholder="" class="form-control input-xlarge bfh-phone" data-format="+7 (ddd) ddd-dddd">
    </div>
</div>
