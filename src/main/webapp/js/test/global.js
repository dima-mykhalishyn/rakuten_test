String.prototype.format = function() {
    var s = this.toString();
    for (var i = 0; i < arguments.length; i++) {
        var reg = new RegExp("\\{" + i + "\\}", "gm");
        s = s.replace(reg, arguments[i]);
    }

    return s;
};

String.prototype.trim = function() {
    return $.trim(this)
};