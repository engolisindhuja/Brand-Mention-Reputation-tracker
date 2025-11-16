<!DOCTYPE html>
<html>
<head>
    <title>Add Mention</title>
</head>
<body>

<h2>Add a Brand Mention</h2>

<form action="addMention" method="post">

    <label>Platform:</label>
    <select name="platform" required>
        <option value="Twitter">Twitter</option>
        <option value="Instagram">Instagram</option>
        <option value="Facebook">Facebook</option>
        <option value="YouTube">YouTube</option>
    </select>
    <br><br>

    <label>Content:</label><br>
    <textarea name="content" rows="5" cols="40" required></textarea>
    <br><br>

    <button type="submit">Submit</button>

</form>

</body>
</html>
