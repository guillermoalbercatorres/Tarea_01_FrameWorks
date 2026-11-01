const sections = {
  aulas: {
    label: 'Aulas', endpoint: '/aula', singular: 'el aula',
    fields: [
      { key: 'nombre', label: 'Nombre', required: true, primary: true },
      { key: 'capacidad', label: 'Capacidad', type: 'number', required: true },
      { key: 'ordenadores', label: 'Tiene ordenadores', type: 'checkbox' }
    ], columns: ['nombre', 'capacidad', 'ordenadores']
  },
  profesores: {
    label: 'Profesores', endpoint: '/profesor', singular: 'el profesor',
    fields: [
      { key: 'nombre', label: 'Nombre completo', required: true, primary: true },
      { key: 'correo', label: 'Correo electrónico', type: 'email', required: true }
    ], columns: ['nombre', 'correo']
  },
  franjas: {
    label: 'Franjas horarias', endpoint: '/franja-horaria', singular: 'la franja horaria',
    fields: [
      { key: 'horaInicio', label: 'Hora de inicio', type: 'time', required: true, primary: true },
      { key: 'horaFin', label: 'Hora de fin', type: 'time', required: true }
    ], columns: ['horaInicio', 'horaFin']
  },
  reservas: {
    label: 'Reservas', endpoint: '/reserva', singular: 'la reserva',
    fields: [
      { key: 'fecha', label: 'Fecha de la reserva', type: 'date', required: true, primary: true },
      { key: 'fechaInsercion', label: 'Fecha de creación', type: 'date', required: true },
      { key: 'profesor', label: 'Profesor', relation: 'profesores', required: true },
      { key: 'aula', label: 'Aula', relation: 'aulas', required: true },
      { key: 'franjaHoraria', label: 'Franja horaria', relation: 'franjas', required: true }
    ], columns: ['fecha', 'profesor', 'aula', 'franjaHoraria']
  }
};

const tabList = document.querySelector('#tabs');
const tableWrap = document.querySelector('#table-wrap');
const formPanel = document.querySelector('#form-panel');
const form = document.querySelector('#record-form');
const notice = document.querySelector('#notice');
const grid = document.querySelector('.content-grid');
let currentKey = 'aulas';
let records = [];
let editingId = null;

function apiUrl(endpoint) {
  return `${window.location.origin}${endpoint}`;
}

async function request(endpoint, options = {}) {
  const response = await fetch(apiUrl(endpoint), {
    ...options,
    headers: { 'Content-Type': 'application/json', ...options.headers }
  });
  if (!response.ok) {
    let detail = '';
    try { detail = await response.text(); } catch (_) { /* respuesta sin cuerpo */ }
    throw new Error(detail || `Error HTTP ${response.status}`);
  }
  if (response.status === 204) return null;
  return response.json();
}

function showNotice(message, type = 'error') {
  notice.textContent = message;
  notice.className = `notice ${type}`;
}

function clearNotice() {
  notice.textContent = '';
  notice.className = 'notice hidden';
}

function setConnection(online) {
  document.querySelector('.status-dot').classList.toggle('online', online);
  document.querySelector('#connection-label').textContent = online ? 'API conectada' : 'API sin conexión';
}

function displayValue(value) {
  if (value == null || value === '') return '—';
  if (typeof value === 'boolean') return value ? 'Sí' : 'No';
  if (typeof value === 'object') return value.nombre || value.correo || `${value.horaInicio || ''}${value.horaFin ? ` – ${value.horaFin}` : ''}` || `#${value.id ?? ''}`;
  return value;
}

function renderTabs() {
  tabList.replaceChildren();
  Object.entries(sections).forEach(([key, section]) => {
    const button = document.createElement('button');
    button.type = 'button';
    button.className = `tab${key === currentKey ? ' active' : ''}`;
    button.textContent = section.label;
    button.addEventListener('click', () => selectSection(key));
    tabList.append(button);
  });
}

function renderTable() {
  const section = sections[currentKey];
  document.querySelector('#section-title').textContent = section.label;
  document.querySelector('#record-count').textContent = `${records.length} ${records.length === 1 ? 'registro' : 'registros'}`;
  if (!records.length) {
    tableWrap.innerHTML = '<div class="empty">No hay registros todavía. Crea el primero con el botón de arriba.</div>';
    return;
  }
  const table = document.createElement('table');
  const head = document.createElement('thead');
  const headerRow = document.createElement('tr');
  [...section.columns.map(key => section.fields.find(field => field.key === key).label), 'Acciones'].forEach(label => {
    const th = document.createElement('th'); th.textContent = label; headerRow.append(th);
  });
  head.append(headerRow);
  const body = document.createElement('tbody');
  records.forEach(record => {
    const row = document.createElement('tr');
    section.columns.forEach((key, index) => {
      const cell = document.createElement('td');
      const value = record[key];
      cell.textContent = displayValue(value);
      if (index === 0) cell.className = 'primary-cell';
      row.append(cell);
    });
    const actions = document.createElement('td');
    actions.className = 'row-actions';
    const edit = document.createElement('button'); edit.type = 'button'; edit.textContent = 'Editar';
    edit.addEventListener('click', () => openForm(record));
    const remove = document.createElement('button'); remove.type = 'button'; remove.textContent = 'Eliminar'; remove.className = 'delete';
    remove.addEventListener('click', () => deleteRecord(record));
    actions.append(edit, remove); row.append(actions); body.append(row);
  });
  table.append(head, body);
  tableWrap.replaceChildren(table);
}

async function loadRecords() {
  clearNotice();
  tableWrap.innerHTML = '<div class="empty">Cargando registros…</div>';
  try {
    records = await request(sections[currentKey].endpoint);
    setConnection(true);
    renderTable();
  } catch (error) {
    setConnection(false);
    records = [];
    renderTable();
    showNotice(`No se pudieron cargar los datos. Comprueba que Spring Boot y MySQL están iniciados. ${error.message}`);
  }
}

async function selectSection(key) {
  currentKey = key;
  closeForm();
  renderTabs();
  await loadRecords();
}

function makeInput(field, value) {
  let input;
  if (field.relation) {
    input = document.createElement('select');
    const empty = document.createElement('option'); empty.value = ''; empty.textContent = 'Selecciona una opción'; input.append(empty);
    (window.relatedRecords[field.relation] || []).forEach(item => {
      const option = document.createElement('option'); option.value = item.id; option.textContent = displayValue(item); input.append(option);
    });
    input.value = value?.id ?? '';
  } else {
    input = document.createElement('input');
    input.type = field.type || 'text';
    if (input.type === 'checkbox') {
      input.checked = Boolean(value);
    } else {
      if (input.type === 'number') input.min = '1';
      input.value = value ?? '';
    }
  }
  input.name = field.key;
  input.required = Boolean(field.required);
  return input;
}

window.relatedRecords = {};

async function openForm(record = null) {
  editingId = record?.id ?? null;
  clearNotice();
  form.replaceChildren();
  const section = sections[currentKey];
  if (currentKey === 'reservas') {
    try {
      const relations = await Promise.all(['aulas', 'profesores', 'franjas'].map(async key => [key, await request(sections[key].endpoint)]));
      relations.forEach(([key, values]) => { window.relatedRecords[key] = values; });
    } catch (error) {
      showNotice(`No se pudieron cargar aulas, profesores y franjas para la reserva. ${error.message}`);
      return;
    }
  }
  document.querySelector('#form-title').textContent = record ? 'Editar registro' : 'Nuevo registro';
  section.fields.forEach(field => {
    const label = document.createElement('label'); label.className = 'field';
    const caption = document.createElement('span'); caption.textContent = field.label;
    label.append(caption, makeInput(field, record?.[field.key])); form.append(label);
  });
  const actions = document.createElement('div'); actions.className = 'form-actions';
  const cancel = document.createElement('button'); cancel.type = 'button'; cancel.className = 'button button-secondary'; cancel.textContent = 'Cancelar'; cancel.addEventListener('click', closeForm);
  const save = document.createElement('button'); save.type = 'submit'; save.className = 'button button-primary'; save.textContent = 'Guardar';
  actions.append(cancel, save); form.append(actions);
  formPanel.classList.remove('hidden'); grid.classList.add('with-form');
  if (window.innerWidth < 760) formPanel.scrollIntoView({ behavior: 'smooth', block: 'start' });
}

function closeForm() {
  editingId = null;
  formPanel.classList.add('hidden');
  grid.classList.remove('with-form');
}

async function saveRecord(event) {
  event.preventDefault();
  const section = sections[currentKey];
  const data = {};
  for (const field of section.fields) {
    const input = form.elements[field.key];
    const value = input.value;
    data[field.key] = field.type === 'checkbox' ? input.checked
      : field.relation ? { id: Number(value) }
      : field.type === 'number' ? Number(value)
      : value;
  }
  try {
    await request(editingId ? `${section.endpoint}/${editingId}` : section.endpoint, {
      method: editingId ? 'PUT' : 'POST', body: JSON.stringify(data)
    });
    closeForm();
    await loadRecords();
    showNotice('Registro guardado correctamente.', 'success');
  } catch (error) {
    showNotice(`No se pudo guardar. ${error.message}`);
  }
}

async function deleteRecord(record) {
  const section = sections[currentKey];
  if (!window.confirm(`¿Quieres eliminar ${section.singular} #${record.id}?`)) return;
  try {
    await request(`${section.endpoint}/${record.id}`, { method: 'DELETE' });
    await loadRecords();
    showNotice('Registro eliminado.', 'success');
  } catch (error) {
    showNotice(`No se pudo eliminar. Puede que tenga reservas asociadas. ${error.message}`);
  }
}

document.querySelector('#new-button').addEventListener('click', () => openForm());
document.querySelector('#refresh-button').addEventListener('click', loadRecords);
document.querySelector('#close-form').addEventListener('click', closeForm);
form.addEventListener('submit', saveRecord);
renderTabs();
loadRecords();
