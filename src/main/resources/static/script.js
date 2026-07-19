function apiBase(){
    // Empty = same server the page was loaded from (relative URLs like /users)
    return document.getElementById('apiBase').value.trim().replace(/\/+$/,'');
}

function log(msg, ok=true){
    const el = document.getElementById('log');
    const line = document.createElement('div');
    line.className = ok ? 'ok' : 'err';
    const time = new Date().toLocaleTimeString();
    line.textContent = `[${time}] ${msg}`;
    el.prepend(line);
}

async function api(path, options={}){
    const url = apiBase() + path;
    try{
        const res = await fetch(url, {
            headers: {'Content-Type':'application/json'},
            ...options
        });
        let data = null;
        const text = await res.text();
        try { data = text ? JSON.parse(text) : null; } catch(e){ data = text; }
        if(!res.ok){
            log(`${options.method||'GET'} ${path} → ${res.status}`, false);
            throw new Error((data && data.message) || `HTTP ${res.status}`);
        }
        log(`${options.method||'GET'} ${path} → ${res.status} OK`);
        return data;
    }catch(err){
        log(`${options.method||'GET'} ${path} failed: ${err.message}`, false);
        throw err;
    }
}

async function checkConnection(){
    const pill = document.getElementById('status-pill');
    pill.textContent = 'Checking...';
    try{
        await api('/projects');
        pill.textContent = '✅ Connected';
        pill.style.color = 'var(--green)';
    }catch(e){
        pill.textContent = '❌ Not connected';
        pill.style.color = 'var(--red)';
    }
}

/* ---------- USERS ---------- */
async function createUser(){
    const name = document.getElementById('userName').value.trim();
    const email = document.getElementById('userEmail').value.trim();
    if(!name || !email){ alert('Name and email are required'); return; }
    try{
        await api('/users', {method:'POST', body: JSON.stringify({name, email})});
        document.getElementById('userName').value = '';
        document.getElementById('userEmail').value = '';
        loadUsers();
    }catch(e){ alert('Failed to create user: ' + e.message); }
}

async function deleteUser(id){
    if(!confirm(`Delete user #${id}?`)) return;
    try{
        await api(`/users/${id}`, {method:'DELETE'});
        loadUsers();
    }catch(e){ alert('Failed to delete: ' + e.message); }
}

async function loadUsers(){
    const tbody = document.getElementById('usersTable');
    tbody.innerHTML = `<tr><td colspan="4" class="empty">Loading...</td></tr>`;
    try{
        const users = await api('/users');
        if(!users || !users.length){
            tbody.innerHTML = `<tr><td colspan="4" class="empty">No users yet</td></tr>`;
            return;
        }
        tbody.innerHTML = users.map(u => `
      <tr>
        <td>${u.id}</td>
        <td>${u.name}</td>
        <td>${u.email}</td>
        <td><button class="btn-danger btn-sm" onclick="deleteUser(${u.id})">Delete</button></td>
      </tr>
    `).join('');
    }catch(e){
        tbody.innerHTML = `<tr><td colspan="4" class="empty">Couldn't load — check the API URL</td></tr>`;
    }
}

/* ---------- PROJECTS ---------- */
async function loadProjects(){
    const tbody = document.getElementById('projectsTable');
    tbody.innerHTML = `<tr><td colspan="2" class="empty">Loading...</td></tr>`;
    try{
        const projects = await api('/projects');
        if(!projects || !projects.length){
            tbody.innerHTML = `<tr><td colspan="2" class="empty">No projects yet</td></tr>`;
            return;
        }
        tbody.innerHTML = projects.map(p => `<tr><td>${p.id}</td><td>${p.name}</td></tr>`).join('');
    }catch(e){
        tbody.innerHTML = `<tr><td colspan="2" class="empty">Couldn't load — check the API URL</td></tr>`;
    }
}

/* ---------- TICKETS ---------- */
async function createTicket(){
    const title = document.getElementById('ticketTitle').value.trim();
    const description = document.getElementById('ticketDesc').value.trim();
    const projectId = document.getElementById('ticketProjectId').value.trim();
    if(!title || !projectId){ alert('Title and Project ID are required'); return; }
    try{
        await api('/tickets', {method:'POST', body: JSON.stringify({
                title, description, projectId: Number(projectId)
            })});
        document.getElementById('ticketTitle').value = '';
        document.getElementById('ticketDesc').value = '';
        document.getElementById('ticketProjectId').value = '';
        loadTickets();
    }catch(e){ alert('Failed to create ticket: ' + e.message); }
}

async function updateTicketStatus(id, status){
    try{
        await api(`/tickets/${id}`, {method:'PUT', body: JSON.stringify({status})});
        loadTickets();
    }catch(e){ alert('Failed to update: ' + e.message); }
}

async function deleteTicket(id){
    if(!confirm(`Delete ticket #${id}?`)) return;
    try{
        await api(`/tickets/${id}`, {method:'DELETE'});
        loadTickets();
    }catch(e){ alert('Failed to delete: ' + e.message); }
}

function buildQuery(){
    const title = document.getElementById('filterTitle').value.trim();
    const description = document.getElementById('filterDesc').value.trim();
    const status = document.getElementById('filterStatus').value;
    const params = new URLSearchParams();
    if(title) params.set('title', title);
    if(description) params.set('description', description);
    if(status) params.set('status', status);
    const qs = params.toString();
    return qs ? `?${qs}` : '';
}

async function loadTickets(){
    const tbody = document.getElementById('ticketsTable');
    tbody.innerHTML = `<tr><td colspan="6" class="empty">Loading...</td></tr>`;
    try{
        const tickets = await api('/tickets' + buildQuery());
        if(!tickets || !tickets.length){
            tbody.innerHTML = `<tr><td colspan="6" class="empty">No tickets found</td></tr>`;
            return;
        }
        tbody.innerHTML = tickets.map(t => `
      <tr>
        <td>${t.id}</td>
        <td>${t.title}</td>
        <td>${t.description || ''}</td>
        <td><span class="badge ${t.status}">${t.status}</span></td>
        <td>${t.projectId ?? ''}</td>
        <td class="actions">
          <button class="btn-secondary btn-sm" onclick="updateTicketStatus(${t.id}, 'in_progress')">In progress</button>
          <button class="btn-green btn-sm" onclick="updateTicketStatus(${t.id}, 'closed')">Close</button>
          <button class="btn-danger btn-sm" onclick="deleteTicket(${t.id})">Delete</button>
        </td>
      </tr>
    `).join('');
    }catch(e){
        tbody.innerHTML = `<tr><td colspan="6" class="empty">Couldn't load — check the API URL</td></tr>`;
    }
}

/* init */
checkConnection();
loadUsers();
loadProjects();
loadTickets();